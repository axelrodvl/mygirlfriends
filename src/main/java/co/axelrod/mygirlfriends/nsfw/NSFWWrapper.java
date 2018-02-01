package co.axelrod.mygirlfriends.nsfw;

import co.axelrod.vk.VKPhotoDownloader;
import co.axelrod.vk.config.TokenStorage;
import co.axelrod.mygirlfriends.vk.VKTokenStorage;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static co.axelrod.mygirlfriends.util.FileUtil.PHOTOS_DIR_PATH;
import static co.axelrod.mygirlfriends.util.FileUtil.USERS_DIR_PATH;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 31.01.2018.
 */

@Slf4j
public class NSFWWrapper {
    private String domain;

    public NSFWWrapper(String domain) {
        this.domain = domain;
    }

    public void invokeScript(String code) {
        Integer photosToDownload = 3;
        TokenStorage tokenStorage = new VKTokenStorage();

        VKPhotoDownloader vkPhotoDownloader = new VKPhotoDownloader(domain, tokenStorage, PHOTOS_DIR_PATH, USERS_DIR_PATH);

        try {
            log.debug("Downloading " + photosToDownload + " photos from VK.com");
            vkPhotoDownloader.downloadPhoto(code, photosToDownload);

            String[] cmds = {"/bin/sh", "runOpenNSFW.sh"};

            Process p = Runtime.getRuntime().exec(cmds);

            while(p.isAlive()) {
                // Grab output and print to display
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    log.debug(line);
                }
            }
            log.debug("runOpenNSFW.sh is done");
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
