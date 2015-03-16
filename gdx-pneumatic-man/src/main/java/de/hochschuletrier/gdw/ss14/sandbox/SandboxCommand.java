package de.hochschuletrier.gdw.ss14.sandbox;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.utils.ClassUtils;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.states.SandboxState;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SandboxCommand {
    private static final Logger logger = LoggerFactory.getLogger(SandboxCommand.class);
    private static final HashMap<String, Class> sandboxClasses = new HashMap();
    private static AssetManagerX assetManager;

    public static void init(AssetManagerX assetManager) {
        SandboxCommand.assetManager = assetManager;
        try {
            String packageName = SandboxCommand.class.getPackage().getName();
            for (Class clazz : ClassUtils.findClassesInPackage(packageName)) {
                if (!Modifier.isAbstract(clazz.getModifiers()) && SandboxGame.class.isAssignableFrom(clazz)) {
                    sandboxClasses.put(clazz.getSimpleName(), clazz);
                }
            }
        } catch (IOException ex) {
            logger.error("Can't find sandbox classes", ex);
        }

        Main.getInstance().console.register(sandbox_f);
    }
    
    public static void shutdown() {
        Main.getInstance().console.unregister(sandbox_f);
    }

    private static ConsoleCmd sandbox_f = new ConsoleCmd("sandbox", 0, "Run a sandbox game.", 1) {
        @Override
        public void showUsage() {
            showUsage("<sandbox name>");
        }

        @Override
        public void complete(String prefix, List<String> results) {
            for (String sbc : sandboxClasses.keySet()) {
                if (sbc.startsWith(prefix)) {
                    results.add(sbc);
                }
            }
        }

        @Override
        public void execute(List<String> args) {
            Main main = Main.getInstance();
            if(main.isTransitioning()) {
                logger.warn("Transition in progress, please wait.");
            }
            String gameName = args.get(1);
            Class clazz = sandboxClasses.get(gameName);
            if (clazz == null) {
                logger.warn("'{}' is not a sandbox class");
                showUsage();
            } else {
                try {
                    SandboxGame game = (SandboxGame) clazz.newInstance();
                    game.init(assetManager);
                    SandboxState state = new SandboxState(assetManager, game);
                    logger.info("starting sandbox {}", gameName);
                    
                    main.changeState(state);
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("Could not create instance of class", e);
                }
            }
        }
    };
}
