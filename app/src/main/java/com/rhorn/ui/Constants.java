/* https://www.truiton.com/2014/10/android-foreground-service-example/
https://stackoverflow.com/questions/20857120/what-is-the-proper-way-to-stop-a-service-running-as-foreground
 */

package com.rhorn.ui;

public class Constants {
    public interface ACTION {
        String STARTFOREGROUND_ACTION = "com.rhorn.Ghetto.runGhettoRecorder.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.rhorn.Ghetto.runGhettoRecorder.action.stopforeground";
    }
    public interface NOTIFICATION_ITEMS {
        int NOTIFICATION_ID = 456;
        String CHANNEL_ID = "654";
    }
}