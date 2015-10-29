package cmd;

import wraps.C3POWrap;
import wraps.FITSWrap;
import wraps.PhotohawkWrap;
import dao.ImageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;
import utils.PhotoConfigurator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class photohawkplusCmd {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ScheduledExecutorService executorService = Executors
            .newSingleThreadScheduledExecutor();
    List<ImageBean> images;
    C3POWrap c3poWrap;
    PhotohawkWrap photohawkWrap;
    FITSWrap fitsWrap;
    PhotoConfigurator cfg = PhotoConfigurator.getConfigurator();

    public photohawkplusCmd() {
        try {
            Files.createDirectories(Paths.get(cfg.getProperty(Constants.PATH_TMP_PHOTO))).toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Please specify the following:/path/to/originals /path/to/results /path/to/tmp_results /path/to/tmp_image_results");
            return;
        }
    }



    public List<ImageBean> run() {


        executorService.execute(new Runnable() {
            @Override
            public void run() {
                fitsWrap = new FITSWrap();
                c3poWrap = new C3POWrap("localhost", "27017", "c3po", Constants.PATH_FITS_RESULTS);
                photohawkWrap = new PhotohawkWrap();
                images = new ArrayList<>();
                fitsWrap.execute();
                c3poWrap.execute();
                photohawkWrap.execute();
                cfg.setProperty(Constants.WEB_AJAX_STATUS, "");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                photohawkWrap.shutdownExecutor();
            }


        });
        return images;
    }

    public Boolean isReady(){
        return photohawkWrap.isStarted();
    }


}
