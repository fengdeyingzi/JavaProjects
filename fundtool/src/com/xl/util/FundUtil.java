package com.xl.util;



import java.util.HashMap;
import java.util.Map;

import com.xl.tool.OConnect;

/**
 * @author lhb
 * @date 2020-09-06 21:18:51
 * @description
 */
public class FundUtil {

    public static String fundInfoUrl = "http://fundgz.1234567.com.cn/js/%s.js";

    public static String fundDetailUrl = "http://fund.eastmoney.com/%s.html?spm=search";

    /**
     * 获取基金详情
     * @author lhb
     * @date 2020-09-30 15:38:08
     * @param fundCode
     * @return com.fundtool.plugin.domain.FundInfo
     */
    public static String getFundNameByCode(String fundCode, OConnect.PostGetInfoListener listener) {
        String requestUrl = String.format(fundInfoUrl, fundCode);
        OConnect connect = new OConnect(requestUrl,listener);
        /*new OConnect.PostGetInfoListener() {
			
			@Override
			public void onPostGetText(String text) {
				// TODO Auto-generated method stub
				String resultStr = text;
				
				if (resultStr.length()>0) {
					resultStr = resultStr.substring(resultStr.indexOf("(") + 1, resultStr.lastIndexOf(")"));
					System.out.println(resultStr);
//		            assert resultStr != null;
//		            ObjectMapper mapper = new ObjectMapper();
//		            resultStr = resultStr.substring(resultStr.indexOf("(") + 1, resultStr.lastIndexOf(")"));
//		            JsonNode jsonNode = null;
//		            try {
//		                jsonNode = mapper.readTree(resultStr);
//		            } catch (JsonProcessingException e) {
//		                e.printStackTrace();
//		            }
//		            if(jsonNode!=null){
//		                FundInfo fundInfo = new FundInfo();
//		                fundInfo.setFundCode(fundCode);
//		                fundInfo.setFundName(jsonNode.path("name").textValue());
//		                fundInfo.setNetWorth(jsonNode.path("gsz").textValue());
//		                fundInfo.setGrowthRate(jsonNode.path("gszzl").textValue());
//		                fundInfo.setUpdateTime(jsonNode.path("gztime").textValue());
//		                return fundInfo;
//		            }
		        }
			}
		});
		*/
        connect.start();
//        String resultStr = HttpUtil.sendGetRequest(requestUrl);
        
        return null;
    }


    /**
     * 获取基金近期涨跌幅度
     * @author lhb
     * @date 2020-09-30 15:39:06
     * @param fundCode
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    public static Map<String,String> getFundDetailByCode(String fundCode) {
        Map<String, String> result = new HashMap<String,String>();
        String key,value;
        String requestUrl = String.format(fundDetailUrl, fundCode);
        OConnect connect = new OConnect(requestUrl,new OConnect.PostGetInfoListener() {
			
			@Override
			public void onPostGetText(String text) {
				
				
				String resultStr = text;
				
				if (resultStr.length()>0) {
					resultStr = resultStr.substring(resultStr.indexOf("(") + 1, resultStr.lastIndexOf(")"));
					System.out.println(resultStr);
				}
				
			}
		});
//        String resultStr = HttpUtil.sendGetRequest(requestUrl);
//        if (StringUtil.isNotEmpty(resultStr)) {
//            assert resultStr != null;
//            Document doc = Jsoup.parse(resultStr);
//            Element firstTable = doc.select("div.singleStyleHeight01 table").first();
//            Elements trs = firstTable.select("tr");
//            trs.remove(0);
//            for (Element element : trs) {
//                key = element.select("td.alignLeft").text();
//                value = element.select("td.RelatedInfo").select("span").text();
//                result.put(key, value);
//            }
//            return result;
//        }
        return null;
    }
}
