package com.Ichif1205.anrakutei;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;

/**
 * ステージ情報のXMLをパースする
 */
public class StageXmlParser {
	private static final String TAG = StageXmlParser.class.getSimpleName();
	private Context mContext;
	private static final String STAGE_XML_NAME = "stage.xml";
	private static final String STAGE_TAG = "Stage";
	
	public StageXmlParser(Context context) {
		mContext = context;
	}
	
	/**
	 * StageXmlをパースする
	 * @return
	 */
	public void parseStageXml() {
		final XmlPullParser xmlPullParser = Xml.newPullParser();
		final SparseArray<StageInfo> stages = StageInfos.getInstance();
		final AssetManager assets = mContext.getResources().getAssets();
		
		try {
			final InputStream is = assets.open(STAGE_XML_NAME);
			final InputStreamReader isr = new InputStreamReader(is);
			
			xmlPullParser.setInput(isr);
			int eventType;
			while ((eventType = xmlPullParser.next()) != XmlPullParser.END_DOCUMENT) {
				// itemタグを見つけたら、addItemsへ
				if (eventType == XmlPullParser.START_TAG
						&& STAGE_TAG.equals(xmlPullParser.getName())) {
					final StageInfo stage = addStage(xmlPullParser);
					stages.put(stage.id, stage);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			// TODO XMLを読み込みなかった場合、アプリを終了する。
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			// TODO XMLを読み込みなかった場合、アプリを終了する。
		} finally {
			Log.d(TAG, "AssetsManager Close");
			// AssetsManagerをクローズ
//			assets.close();
		}
	}
	
	/**
	 * パース結果をStageInfoオブジェクトとして返す
	 * @param parser
	 * @return
	 */
	private StageInfo addStage(XmlPullParser parser) {
		StageInfo data = new StageInfo();

		while (true) {
			try {
				Log.d(TAG, "XML Loop");
				int eventType = parser.next();
				if (eventType == XmlPullParser.END_TAG
						&& STAGE_TAG.equals(parser.getName())) {
					break;
				}

				// IDを格納
				if (eventType == XmlPullParser.START_TAG
						&& "Id".equals(parser.getName())) {
					eventType = parser.next();
					if (eventType == XmlPullParser.TEXT) {
						data.id = Integer.parseInt(parser.getText());
					}
				}

				// maxInvaderを格納
				if (eventType == XmlPullParser.START_TAG
						&& "MaxInvader".equals(parser.getName())) {
					eventType = parser.next();
					if (eventType == XmlPullParser.TEXT) {
						data.maxInvader = Integer.parseInt(parser.getText());
					}
				}

				// boss情報を格納
				if (eventType == XmlPullParser.START_TAG
						&& "boss".equals(parser.getName())) {
					eventType = parser.next();
					if (eventType == XmlPullParser.TEXT) {
						data.boss = Integer.parseInt(parser.getText());
					}
				}

				// TODO 要素増やしたら修正
//				if (eventType == XmlPullParser.START_TAG
//						&& "date".equals(parser.getName())) {
//					eventType = parser.next();
//					if (eventType == XmlPullParser.TEXT) {
//						data.setDate(parser.getText());
//					}
//				}
//
//				if (eventType == XmlPullParser.START_TAG
//						&& "bookmarkcount".equals(parser.getName())) {
//					eventType = parser.next();
//					if (eventType == XmlPullParser.TEXT) {
//						data.setUsers(parser.getText());
//					}
//				}
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;	
	}
}
