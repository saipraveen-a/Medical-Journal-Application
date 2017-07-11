package com.medplus.journals.utils;

import com.medplus.journals.Application;

import java.io.File;

public class FileUtils {
  public static boolean createDirectoryIfNotExist(File dir) {
    if (!dir.exists()) {
      boolean created = dir.mkdirs();
      if (!created) {
        return false;
      }
    }
    return true;
  }

  public static String getFileName(long publisherId, String uuid) {
    return getDirectory(publisherId) + "/" + uuid + ".pdf";
  }

  public static String getDirectory(long publisherId) {
    return Application.ROOT + "/" + publisherId;
  }
}
