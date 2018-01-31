package co.axelrod.mygirlfriends.nsfw;

import co.axelrod.vk.VKPhotoDownloader;
import co.axelrod.vk.config.TokenStorage;
import co.axelrod.mygirlfriends.vk.VKTokenStorage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static co.axelrod.mygirlfriends.util.FileUtil.PHOTOS_DIR_PATH;
import static co.axelrod.mygirlfriends.util.FileUtil.USERS_DIR_PATH;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 31.01.2018.
 */
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
            System.out.println("Domain: " + domain);
            System.out.println("Code: " + code);

            System.out.println("Downloading photos from VK");
            vkPhotoDownloader.downloadPhoto(code, photosToDownload);

            String[] cmds = {"/bin/sh", "runOpenNSFW.sh", domain, code};

            Process p = Runtime.getRuntime().exec(cmds);

            while(p.isAlive()) {
                // Grab output and print to display
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            System.out.println ("Bash script running is done");
        }
        catch (Exception e) {
            System.out.println ("Err: " + e.getMessage());
        }
    }
}
