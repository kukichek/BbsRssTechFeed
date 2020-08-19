package org.coursera.bbcrsstechfeed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    private Button latestNewsButton;
    private Button archiveNewsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latestNewsButton = (Button) findViewById(R.id.latestNewsButton);
        archiveNewsButton = (Button) findViewById(R.id.archiveNewsButton);

        setOnClickListeners();
    }

    private void setOnClickListeners() {
        latestNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsEnabled(false);

                GetHttpConnectionTask task = new GetHttpConnectionTask();
                task.execute();
            }
        });

        archiveNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getArchiveNewsStartingIntent());
            }
        });
    }

    private void setButtonsEnabled(boolean b) {
        latestNewsButton.setEnabled(b);
        archiveNewsButton.setEnabled(b);
    }

    // TODO: name method properly
    private void showToast() {
        Toast toast = Toast.makeText(this, R.string.unableToConnectToast, Toast.LENGTH_SHORT);
        toast.show();
    }

    private class GetHttpConnectionTask extends AsyncTask<Void, Void, ArrayList<Item>> {
        @Override
        protected ArrayList<Item> doInBackground(Void... voids) {
            try {
                URL rssFeedUrl = new URL("https://feeds.bbci.co.uk/news/technology/rss.xml");

                try (InputStream in = rssFeedUrl.openStream()) {
                    return DOMUtils.parseItems(in);
                } catch (SAXException | ParserConfigurationException | IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } catch (IOException e) { // must not be catched, URL is valid
                return null;
            }
        }

        // TODO: Добавить нормальную документацию к методам
        /**
         * @param items - page content, полученное при помощи метода doInBackground
         *             Если text пуст, то выводит на экран сообщение о неудавшейся попытке соединения;
         *             иначе создает Activity для отображения текста
         */
        @Override
        protected void onPostExecute(ArrayList<Item> items) {
            if (items == null) {
                showToast();
            } else {
                Intent latestNewsActivityIntent = getLatestNewsStartingIntent();

                latestNewsActivityIntent.putParcelableArrayListExtra(LatestNewsActivity.extrasItems, items);
                startActivity(latestNewsActivityIntent);
            }

            setButtonsEnabled(true);
        }
    }

    private Intent getLatestNewsStartingIntent() {
        return new Intent(this, LatestNewsActivity.class);
    }

    private Intent getArchiveNewsStartingIntent() {
        return new Intent(this, ArchiveNewsActivity.class);
    }
}