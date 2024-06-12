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
        Elements tr = document.getElementById(tableId).getElementsByTag("tbody").get(0).getElementsByTag("tr");

        for (Element element : tr) {

            JSONObject data = new JSONObject();

            Set<String> keys = jsons.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Integer value = jsons.getInt(key);

                data.putOpt(key, element.getElementsByTag("td").get(value).text().replace("●", ""));
            }
            Func.save(id, data, type);
        }

        page++;

        Element element = document.getElementById("main-table_paginate");
        if (element == null) {
            return ;
        }
        Elements pageElements = element.getElementsByClass("paginate_page");
        if (pageElements.size() <= 0) {
            return ;
        }
        Elements pageAElements = pageElements.get(0).getElementsByTag("a");
        if (pageAElements.size() < page) {
            return;
        }
    }

}

load(id, url, jsons, type, tableId)
