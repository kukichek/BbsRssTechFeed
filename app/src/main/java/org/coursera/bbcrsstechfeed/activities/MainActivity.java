package org.coursera.bbcrsstechfeed.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.coursera.bbcrsstechfeed.item.Item;
import org.coursera.bbcrsstechfeed.R;
import org.coursera.bbcrsstechfeed.item.parser.XMLUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Launcher activity for an app
 * Displays buttons for viewing latest news and
 * news from the archive
 */
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

    /**
     * Sets onClickListeners to buttons on the activity
     */
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

    /**
     * Sets buttons on the activity enabled or disabled
     */
    private void setButtonsEnabled(boolean b) {
        latestNewsButton.setEnabled(b);
        archiveNewsButton.setEnabled(b);
    }

    /**
     * Shows a toast that an internet connection can't be established
     */
    private void showToastUnableConnection() {
        Toast toast = Toast.makeText(this, R.string.unableToConnectToast, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Class that attempts to establish an internet connection and get news from the site
     * <a href="https://feeds.bbci.co.uk/news/technology/rss.xml">https://feeds.bbci.co.uk/news/technology/rss.xml</a>
     */
    private class GetHttpConnectionTask extends AsyncTask<Void, Void, ArrayList<Item>> {
        /**
         * Establishes internet connection and receives news list;
         * if internet connection can't be established, return null
         *
         * @return List of news items if internet connection is established; otherwise null
         */
        @Override
        protected ArrayList<Item> doInBackground(Void... voids) {
            try {
                URL rssFeedUrl = new URL("https://feeds.bbci.co.uk/news/technology/rss.xml");

                try (InputStream in = rssFeedUrl.openStream();
                     Reader reader = new BufferedReader(new InputStreamReader(in))) {
                    return XMLUtils.parseItems(reader);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    return null;
                }


            } catch (IOException e) { // exception must not be thrown, URL is valid
                return null;
            }
        }

        /**
         * Start new LatestNewsActivity if the list of news isn't null;
         * otherwise shows toast that an internet connection can't be established
         *
         * @param items - page content obtained using the doInBackground method
         *              If the text is empty, then displays a message about a failed connection attempt;
         *              otherwise an Activity is created to display the text
         */
        @Override
        protected void onPostExecute(@Nullable ArrayList<Item> items) {
            if (items == null) {
                showToastUnableConnection();
            } else {
                Intent latestNewsActivityIntent = getLatestNewsStartingIntent()
                        .putParcelableArrayListExtra(LatestNewsActivity.extrasItems, items);
                startActivity(latestNewsActivityIntent);
            }

            setButtonsEnabled(true);
        }
    }

    /**
     * Create intent for starting LatestNewsActivity
     */
    private Intent getLatestNewsStartingIntent() {
        return new Intent(this, LatestNewsActivity.class);
    }

    /**
     * Create intent for starting ArchiveNewsActivity]
     */
    private Intent getArchiveNewsStartingIntent() {
        return new Intent(this, ArchiveNewsActivity.class);
    }
}