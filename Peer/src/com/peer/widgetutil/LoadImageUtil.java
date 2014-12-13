package com.peer.widgetutil;

import android.content.Context;
import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.peer.BuildConfig;
import com.peer.R;


public class LoadImageUtil {
	public static DisplayImageOptions options;
	public static ImageLoader imageLoader;
	public static void initImageLoader(Context context) {
		// TODO Auto-generated method stub
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO);
		if (BuildConfig.DEBUG) {
			builder.writeDebugLogs();
		}
		ImageLoader.getInstance().init(builder.build());

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.mini_avatar_shadow)
				.showImageForEmptyUri(R.drawable.mini_avatar_shadow)
				.showImageOnFail(R.drawable.mini_avatar_shadow).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.ARGB_8888)
				.build();
		imageLoader = ImageLoader.getInstance();
	}
}
