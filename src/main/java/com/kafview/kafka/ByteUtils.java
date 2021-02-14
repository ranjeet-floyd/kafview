package com.kafview.kafka;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteUtils {
  public static String readString(ByteBuffer buffer) {
    return new String(readBytes(buffer), StandardCharsets.UTF_8);
  }

  public static byte[] readBytes(ByteBuffer buffer) {
    return readBytes(buffer, buffer.limit());
  }

  public static byte[] readBytes(ByteBuffer buffer, int size) {
    final byte[] dest = new byte[size];
    if (buffer.hasArray()) {
      System.arraycopy(buffer.array(), buffer.arrayOffset(), dest, 0, size);
    } else {
      buffer.mark();
      buffer.get(dest);
      buffer.reset();
    }
    return dest;
  }

  public  static byte[] convertToByteArray(ByteBuffer buffer) {
    final byte[] bytes = new byte[buffer.remaining()];
    buffer.get(bytes, 0, bytes.length);
    return bytes;
  }
}
