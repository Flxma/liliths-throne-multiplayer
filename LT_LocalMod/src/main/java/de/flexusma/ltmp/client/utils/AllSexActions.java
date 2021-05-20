package de.flexusma.ltmp.client.utils;

import com.lilithsthrone.game.sex.Sex;
import com.lilithsthrone.game.sex.sexActions.SexAction;
import com.lilithsthrone.game.sex.sexActions.SexActionInterface;
import com.lilithsthrone.game.sex.sexActions.baseActions.ClitAnus;
import com.lilithsthrone.main.Main;
import javafx.application.Platform;
import org.reflections.Reflections;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class AllSexActions {

    private HashMap<String,Field> allSA;

    //can only init when in sex

    public AllSexActions(){
        this.allSA=new HashMap<>();
        String[] loca = new String[]{
                "baseActions",
                "baseActionsMisc",
                "baseActionsSelf",
                "baseActionsSelfPartner",
                "baseActionsSelfPlayer",
                "extra"
        };
        for(String pkg:loca) {
            List<Class<?>> allClasses = new ArrayList<>();
            if (pkg.equals("extra")) {
                //manually adding extra actions
                try {
                    allClasses.add(Class.forName("com.lilithsthrone.game.sex.sexActions.SexActionPresets"));
                    allClasses.add(Class.forName("com.lilithsthrone.game.sex.sexActions.SexActionUtility"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {

                try {
                    allClasses = findClasses(new File("./src/main/java/com/lilithsthrone/game/sex/sexActions/" + pkg), "com.lilithsthrone.game.sex.sexActions." + pkg);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

                for (Class<?> ac : allClasses) {
                    Platform.runLater(() -> Logger.log(LogType.DEBUG, "Checking class: " + ac.getName()));
                    Field[] fields = ac.getDeclaredFields();
                    for (Field f : fields) {
                        String name = f.getName();
                        if (f.getType() == SexAction.class) {
                            allSA.put(name, f);
                        }
                    }

                }

        }


    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }else if (file.getName().endsWith(".java")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 5)));
            }
        }
        return classes;
    }

    public SexAction getFromName(String name){
        try {
            return (SexAction) allSA.get(name).get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFromObject(SexAction act){

        if(act.getActionTitle().equals("Manage clothing")){
            return "Manage Clothing";
        }


        for(String name: allSA.keySet()){
            try {
                SexAction laction = ((SexAction)allSA.get(name).get(null));
                Logger.log(LogType.DEBUG,"List Element desc: ["+laction.getActionTitle()+"] testformatch: ["+act.getActionTitle()+"]");
                if(laction.getActionTitle().equals(act.getActionTitle())){
                    return name;
                }
            } catch (Exception e) {
                Logger.log(LogType.WARN,"Error initializing class: "+ e.getLocalizedMessage() );
            }
        }
        return "";
    }

}
