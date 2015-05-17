package jozwiak.marcin.mad_zadanie_3.utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Marcin on 2015-05-13.
 */
public class ImageLoader {
    public byte[] DownloadFromUrl(String imageURL, String iconId) {

        InputStream is = null;

        try {
            HttpGet httpRequest = new HttpGet(imageURL + iconId + ".png");
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = (HttpResponse) httpclient
                    .execute(httpRequest);
            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);

            is = bufferedHttpEntity.getContent();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ( is.read(buffer) != -1)
                baos.write(buffer);

            return baos.toByteArray();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }


        return null;

    }
}
