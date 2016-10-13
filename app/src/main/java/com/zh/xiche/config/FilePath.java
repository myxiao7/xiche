package com.zh.xiche.config;

import java.io.File;

import android.os.Environment;

public class FilePath {
	public static final String CACHE_PATH=Environment.getExternalStorageDirectory().getPath()+File.separator+ "xiche" +File.separator;
}
