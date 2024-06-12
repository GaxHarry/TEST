import cn.hutool.json.JSONObject
import com.common.utils.Func
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

def load(Long id, String url, JSONObject jsons, String type, String tableId) {
    Integer page = 1;

    Document document = null;
    while (true) {
        document = Func.loadUrl(url, page);
        Elements tr = document.getElementById(tableId).getElementsByClass("news_item");

        for (Element element : tr) {
            JSONObject data = new JSONObject();

            String newsTime = element.getElementsByClass("news_time").get(0).text();
            String content = element.getElementsByClass("news_detail_text").get(0).text();
            data.putOpt("newsTime", newsTime);
            data.putOpt("content", content);
            Func.save(id, data, type);
        }
        return;
    }

}

load(id, url, jsons, type, tableId)
