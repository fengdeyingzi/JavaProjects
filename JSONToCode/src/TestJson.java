import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

class JsonObject {
	String msg;
	int code;
	List<Data> data;

	class Data {
		String notify_url;

		public Data fromJson(JSONObject json) {
			notify_url = json.getString("notify_url");
			return this;

		}

	}

	public JsonObject fromJson(JSONObject json) {
		msg = json.getString("msg");
		code = json.getInt("code");
		List<Data> data = new ArrayList<Data>();
		if (json.has("data")) {
			JSONArray arr_temp = json.getJSONArray("data");
			for (int i = 0; i < arr_temp.length(); i++) {
				JSONObject obj_item = arr_temp.getJSONObject(i);
				data.add(new Data().fromJson(obj_item));
			}
		}
		return this;

	}

}