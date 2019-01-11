package org.enumus.samples;

import org.enumus.initializer.Initializable;
import org.enumus.initializer.Value;

import static org.enumus.samples.OsType.Unix;
import static org.enumus.samples.OsType.Windows;

public enum Software implements Initializable {
    MS_WORD("Microsoft Word", "Microsoft Office", "HKLM/Microsoft/Office/Word", "/Program Files/Microsoft Office/Microsoft Word", new OsType[] {Windows}, null),
    //INTELLIJ("IntelliJ IDEA", null, null, null, new OsType[] {OsType.Windows, OsType.Unix}, RuntimeEnvironment.JAVA),


    @Value(name = "title", value = "IntelliJ IDEA")
    //@Value(name = "operatingSystem", value = {"Windows", "Unix"}, type = OsType.class)
    @Platform(name = "operatingSystem", os = {Windows, Unix})
    @Environment(name = "environemnt", value = RuntimeEnvironment.JAVA)
    INTELLIJ(){},

    ECLIPSE(),
    ;


    private final String title;
    private final String parentTitle;
    private final String registryPath;
    private final String path;
    private final OsType[] operatingSystems;
    private final RuntimeEnvironment environment;

    Software() {
        //this($(), $(null), $(null), $(null), $(null), $(null));

        this.title = $();
        this.parentTitle = $();
        this.registryPath = $();
        this.path = $();
        this.operatingSystems = $();
        this.environment = $();

    }


    Software(String title, String parentTitle, String registryPath, String path, OsType[] operatingSystems, RuntimeEnvironment environment) {
        init();
        //System.out.println(getClass() + ", " + getDeclaringClass());
        //System.out.println(Arrays.asList(getClass().getEnumConstants()[0].getClass().getAnnotations()));

//        try {
//            System.out.println(Arrays.toString(getDeclaringClass().getField(this.name()).getAnnotations()));
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }

        this.title = title;
        this.parentTitle = parentTitle;
        this.registryPath = registryPath;
        this.path = path;
        this.operatingSystems = operatingSystems;
        this.environment = environment;
    }
}
