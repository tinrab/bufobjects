// automatically generated by the FlatBuffers compiler, do not modify
package generated;

import java.nio.*;
import java.lang.*;

import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class FlatBuffersSimple extends Table {
  public static FlatBuffersSimple getRootAsFlatSimple(ByteBuffer _bb) { return getRootAsFlatSimple(_bb, new FlatBuffersSimple()); }
  public static FlatBuffersSimple getRootAsFlatSimple(ByteBuffer _bb, FlatBuffersSimple obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public FlatBuffersSimple __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String name() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer nameAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public double value() { int o = __offset(6); return o != 0 ? bb.getDouble(o + bb_pos) : 0.0; }
  public int Arr(int j) { int o = __offset(8); return o != 0 ? bb.getInt(__vector(o) + j * 4) : 0; }
  public int ArrLength() { int o = __offset(8); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer ArrAsByteBuffer() { return __vector_as_bytebuffer(8, 4); }

  public static int createFlatSimple(FlatBufferBuilder builder,
                                     int nameOffset,
                                     double value,
                                     int ArrOffset) {
    builder.startObject(3);
    FlatBuffersSimple.addValue(builder, value);
    FlatBuffersSimple.addArr(builder, ArrOffset);
    FlatBuffersSimple.addName(builder, nameOffset);
    return FlatBuffersSimple.endFlatSimple(builder);
  }

  public static void startFlatSimple(FlatBufferBuilder builder) { builder.startObject(3); }
  public static void addName(FlatBufferBuilder builder, int nameOffset) { builder.addOffset(0, nameOffset, 0); }
  public static void addValue(FlatBufferBuilder builder, double value) { builder.addDouble(1, value, 0.0); }
  public static void addArr(FlatBufferBuilder builder, int ArrOffset) { builder.addOffset(2, ArrOffset, 0); }
  public static int createArrVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addInt(data[i]); return builder.endVector(); }
  public static void startArrVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endFlatSimple(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishFlatSimpleBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
}

