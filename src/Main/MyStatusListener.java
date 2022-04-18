package Main;

import Resources.Status;
import Resources.StatusListener;

import javax.swing.*;

public class MyStatusListener implements StatusListener {

    private JProgressBar bar;

    public MyStatusListener(JProgressBar bar){
        this.bar = bar;
    }
    @Override
    public void statusChanged(Status s) {
        System.out.println("Status " + s.getProgress());
        bar.setValue(s.getProgress());
    }
}
