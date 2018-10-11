package htl.at.dachsteinscraber

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import kotlin.concurrent.thread
import java.io.IOException
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    private var mediaController:MediaController?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        thread{
            while(true) {
                var source: String? = null
                try {
                    var doc = Jsoup.connect("https://webtv.feratel.com/webtv/?cam=5132&design=v3&c0=0&c2=1&lg=en&s=0").userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0").get()
                    var video = doc.getElementById("fer_video")
                    source = video.select("source").first().attr("src")
                    runOnUiThread {
                        videoView.setVideoPath(source);
                        mediaController = MediaController(this);
                        mediaController?.setAnchorView(videoView);
                        videoView.setMediaController(mediaController);
                        videoView.start()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                println(source)
                Thread.sleep(1000*60);
            }
        }
    }
}
/*.run{
            select("video").forEachIndexed { index, element ->
                println("$index. ${element.text()} (${element.attr("href")})")
            }*/