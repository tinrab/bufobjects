package com.moybl.bufobjects;

import java.nio.*;

public class BufferObjectBuilder {

  public static final int MAX_VAR_INT_BYTES = 5;
  public static final int MAX_VAR_LONG_BYTES = 10;

  private ByteBuffer buffer;
  private int maxCapacity;

  public BufferObjectBuilder(int initialCapacity, int maxCapacity) {
    this.maxCapacity = maxCapacity;
    buffer = ByteBuffer.allocate(initialCapacity);
    buffer.order(ByteOrder.LITTLE_ENDIAN);
  }

  public BufferObjectBuilder() {
    this(1024, 8196);
  }

  public BufferObjectBuilder(ByteBuffer buffer) {
    this.buffer = buffer;
    buffer.clear();
    buffer.order(ByteOrder.LITTLE_ENDIAN);
    maxCapacity = buffer.capacity() * 8;
  }

  public void growBuffer(int reserve) {
    if (buffer.capacity() == maxCapacity) {
      throw new BufferOverflowException();
    }

    int newCapacity = Math.min(maxCapacity, Math
      .max(buffer.capacity() + reserve - buffer.remaining(), buffer.capacity() * 2));
    int pos = buffer.position();

    ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
    newBuffer.order(ByteOrder.LITTLE_ENDIAN);

    buffer.rewind();
    newBuffer.put(buffer);
    newBuffer.position(pos);

    buffer = newBuffer;
  }

  public void writeString(CharSequence value) {
    int strlen = value.length();
    int utflen = 0;
    int c, count = 0;

    for (int i = 0; i < strlen; i++) {
      c = value.charAt(i);
      if ((c >= 0x0001) && (c <= 0x007F)) {
        utflen++;
      } else if (c > 0x07FF) {
        utflen += 3;
      } else {
        utflen += 2;
      }
    }

    if (buffer.remaining() < 2 + utflen) {
      growBuffer(2 + utflen);
    }

    buffer.put((byte) ((utflen >>> 8) & 0xFF));
    buffer.put((byte) (utflen & 0xFF));

    int i = 0;
    for (i = 0; i < strlen; i++) {
      c = value.charAt(i);
      if (!((c >= 0x0001) && (c <= 0x007F))) {
        break;
      }
      buffer.put((byte) c);
    }

    for (; i < strlen; i++) {
      c = value.charAt(i);
      if ((c >= 0x0001) && (c <= 0x007F)) {
        buffer.put((byte) c);

      } else if (c > 0x07FF) {
        buffer.put((byte) (0xE0 | ((c >> 12) & 0x0F)));
        buffer.put((byte) (0x80 | ((c >> 6) & 0x3F)));
        buffer.put((byte) (0x80 | (c & 0x3F)));
      } else {
        buffer.put((byte) (0xC0 | ((c >> 6) & 0x1F)));
        buffer.put((byte) (0x80 | (c & 0x3F)));
      }
    }
  }

  public String readString() {
    int utflen = ((buffer.get() << 8) + buffer.get()) & 0xFFFF;

    byte[] bytearr = new byte[utflen];
    char[] chararr = new char[utflen];

    int c, char2, char3;
    int count = 0;
    int chararrCount = 0;

    buffer.get(bytearr);

    while (count < utflen) {
      c = (int) bytearr[count] & 0xff;
      if (c > 127) break;
      count++;
      chararr[chararrCount++] = (char) c;
    }

    while (count < utflen) {
      c = (int) bytearr[count] & 0xff;
      switch (c >> 4) {
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
          count++;
          chararr[chararrCount++] = (char) c;
          break;
        case 12:
        case 13:
          count += 2;
          if (count > utflen)
            return null;
          char2 = (int) bytearr[count - 1];
          if ((char2 & 0xC0) != 0x80)
            return null;
          chararr[chararrCount++] = (char) (((c & 0x1F) << 6) |
            (char2 & 0x3F));
          break;
        case 14:
          count += 3;
          if (count > utflen)
            return null;
          char2 = (int) bytearr[count - 2];
          char3 = (int) bytearr[count - 1];
          if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
            return null;
          chararr[chararrCount++] = (char) (((c & 0x0F) << 12) |
            ((char2 & 0x3F) << 6) |
            (char3 & 0x3F));
          break;
        default:
          return null;
      }
    }

    return new String(chararr, 0, chararrCount);
  }

  public void writeVarInt32(int value) {
    writeVarUInt32((value << 1) ^ (value >> 31));
  }

  public void writeVarUInt32(int value) {
    do {
      int bits = value & 0x7F;
      value >>>= 7;
      buffer.put((byte) (bits + ((value != 0) ? 0x80 : 0)));
    } while (value != 0);
  }

  public int readVarInt32() {
    int uint = readVarUInt32();
    int tmp = (((uint << 31) >> 31) ^ uint) >> 1;
    return tmp ^ (uint & (1 << 31));
  }

  public int readVarUInt32() {
    int tmp = buffer.get();

    if (tmp >= 0) {
      return tmp;
    }

    int value = tmp & 0x7f;
    if ((tmp = buffer.get()) >= 0) {
      value |= tmp << 7;
    } else {
      value |= (tmp & 0x7f) << 7;
      if ((tmp = buffer.get()) >= 0) {
        value |= tmp << 14;
      } else {
        value |= (tmp & 0x7f) << 14;
        if ((tmp = buffer.get()) >= 0) {
          value |= tmp << 21;
        } else {
          value |= (tmp & 0x7f) << 21;
          value |= (tmp = buffer.get()) << 28;
          while (tmp < 0) {
            tmp = buffer.get();
          }
        }
      }
    }

    return value;
  }

  public void writeVarInt64(long value) {
    writeVarUInt64((value << 1) ^ (value >> 63));
  }

  public void writeVarUInt64(long value) {
    while (true) {
      int bits = ((int) value) & 0x7f;
      value >>>= 7;

      if (value == 0) {
        buffer.put((byte) bits);
        break;
      }

      buffer.put((byte) (bits | 0x80));
    }
  }

  public long readVarInt64() {
    long uint = readVarUInt64();
    long tmp = (((uint << 63) >> 63) ^ uint) >> 1;
    return tmp ^ (uint & (1L << 63));
  }

  public long readVarUInt64() {
    long tmp = buffer.get();

    if (tmp >= 0) {
      return tmp;
    }

    long value = tmp & 0x7f;
    if ((tmp = buffer.get()) >= 0) {
      value |= tmp << 7;
    } else {
      value |= (tmp & 0x7f) << 7;
      if ((tmp = buffer.get()) >= 0) {
        value |= tmp << 14;
      } else {
        value |= (tmp & 0x7f) << 14;
        if ((tmp = buffer.get()) >= 0) {
          value |= tmp << 21;
        } else {
          value |= (tmp & 0x7f) << 21;
          if ((tmp = buffer.get()) >= 0) {
            value |= tmp << 28;
          } else {
            value |= (tmp & 0x7f) << 28;
            if ((tmp = buffer.get()) >= 0) {
              value |= tmp << 35;
            } else {
              value |= (tmp & 0x7f) << 35;
              if ((tmp = buffer.get()) >= 0) {
                value |= tmp << 42;
              } else {
                value |= (tmp & 0x7f) << 42;
                if ((tmp = buffer.get()) >= 0) {
                  value |= tmp << 49;
                } else {
                  value |= (tmp & 0x7f) << 49;
                  if ((tmp = buffer.get()) >= 0) {
                    value |= tmp << 56;
                  } else {
                    value |= (tmp & 0x7f) << 56;
                    value |= ((long) buffer.get()) << 63;
                  }
                }
              }
            }
          }
        }
      }
    }

    return value;
  }

  public void writeBool(boolean value) {
    buffer.put((byte) (value ? 1 : 0));
  }

  public boolean readBool() {
    return buffer.get() == 1;
  }

  public void writeInt8(byte value) {
    buffer.put(value);
  }

  public void writeUInt8(byte value) {
    buffer.put(value);
  }

  public void writeInt16(short value) {
    buffer.putShort(value);
  }

  public void writeUInt16(short value) {
    buffer.putShort(value);
  }

  public void writeInt32(int value) {
    buffer.putInt(value);
  }

  public void writeUInt32(int value) {
    buffer.putInt(value);
  }

  public void writeInt64(long value) {
    buffer.putLong(value);
  }

  public void writeUInt64(long value) {
    buffer.putLong(value);
  }

  public void writeFloat32(float value) {
    buffer.putFloat(value);
  }

  public void writeFloat64(double value) {
    buffer.putDouble(value);
  }

  public byte readInt8() {
    return buffer.get();
  }

  public byte readUInt8() {
    return buffer.get();
  }

  public short readInt16() {
    return buffer.getShort();
  }

  public short readUInt16() {
    return buffer.getShort();
  }

  public int readInt32() {
    return buffer.getInt();
  }

  public int readUInt32() {
    return buffer.getInt();
  }

  public long readInt64() {
    return buffer.getLong();
  }

  public long readUInt64() {
    return buffer.getLong();
  }

  public float readFloat32() {
    return buffer.getFloat();
  }

  public double readFloat64() {
    return buffer.getDouble();
  }

  public void rewind() {
    buffer.rewind();
  }

  public int getOffset() {
    return buffer.position();
  }

  public int getCapacity() {
    return buffer.capacity();
  }

  public ByteBuffer getBuffer() {
    return buffer;
  }

  public byte[] getData() {
    return buffer.array();
  }

  public static int getVarInt32Size(int value) {
    return getVarUInt32Size((value << 1) ^ (value >> 31));
  }

  public static int getVarUInt32Size(int value) {
    int size = 0;

    do {
      size++;
      value >>>= 7;
    } while (value != 0);

    return size;
  }

  public static int getVarInt64Size(long value) {
    return getVarUInt64Size((value << 1) ^ (value >> 63));
  }

  public static int getVarUInt64Size(long value) {
    int size = 0;

    do {
      size++;
      value >>>= 7;
    } while (value != 0);

    return size;
  }

  public static int getStringSize(String value) {
    int strlen = value.length();
    int utflen = 0;
    int c;

    for (int i = 0; i < strlen; i++) {
      c = value.charAt(i);
      if ((c >= 0x0001) && (c <= 0x007F)) {
        utflen++;
      } else if (c > 0x07FF) {
        utflen += 3;
      } else {
        utflen += 2;
      }
    }

    return utflen + 2;
  }

}
