package org.traud.math;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by traud on 4/11/2017.
 */
public class RandomAccessFileBits {
    private long CHUNK_SIZE = 16*1024*1024;
    private long CHUNK_MASK = CHUNK_SIZE-1;

    private final RandomAccessFile raf;
    private final long bitCount;
    private MappedByteBuffer map;
    private long currentChunkindex;
    private final FileChannel.MapMode mapMode;

    public RandomAccessFileBits(long bitCount, RandomAccessFile raf, boolean readOnly) throws IOException {
        this.raf = raf;

        // make sure we fill up every byte
        while ((bitCount % 8) != 0) {
            bitCount++;
        }
        this.bitCount = bitCount;

        long byteCount = bitCount/8;
        byte[] buf = new byte[1024];
        long bufCount = byteCount/1024;
        long bufMod= byteCount%1024;

        System.out.printf("RandomAccessFileBits: %,d bits -> %,d bytes / %,d bufs\n", bitCount, byteCount, bufCount);

        if (readOnly) {
            mapMode = FileChannel.MapMode.READ_ONLY;
        }
        else {
            mapMode = FileChannel.MapMode.READ_WRITE;
            raf.seek(0);
            for (long i = 0; i < bufCount; ++i)
                raf.write(buf);
            for (long i = 0; i < bufMod; ++i)
                raf.write(0);
            raf.getChannel().truncate(byteCount);
        }

        // if size is less than 2GB, map everything into memory
        if (byteCount <= 2L*1024*1024*1024) {
            CHUNK_SIZE = byteCount;
            CHUNK_MASK = -1;
        }
    }


    public void set(long bitIndex) {
        if (mapMode == FileChannel.MapMode.READ_ONLY)
            throw new IllegalArgumentException("open in read-only mode.");

        try {
//            long byteIndex =bitIndex >>> 3;
//            raf.seek(byteIndex);
//            int b = raf.read();
//
//            b |= 1 << (bitIndex & 0x07);
//            raf.seek(byteIndex);
//            raf.write(b);

            long byteIndex =bitIndex >>> 3;
            long chunkIndex = (int)(byteIndex / CHUNK_SIZE);
            mapChunk(chunkIndex);
            int b = map.get((int)(byteIndex & CHUNK_MASK));
            b |= 1 << (bitIndex & 0x07);
            map.put((int)(byteIndex & CHUNK_MASK), (byte)b);
        } catch (IOException e) {
            throw new RuntimeException(e.toString(), e.getCause());
        }

    }

    private void mapChunk(long chunkIndex) throws IOException {
        if (map == null || chunkIndex != currentChunkindex) {
            System.out.printf("mapChunk(0x%x) -> %d / %d. ChunkSize=%d\n", chunkIndex, chunkIndex, ((long)chunkIndex)*CHUNK_SIZE, CHUNK_SIZE);
            this.map = raf.getChannel().map(mapMode, ((long)chunkIndex)*CHUNK_SIZE, CHUNK_SIZE);
            currentChunkindex = chunkIndex;
        }
    }

    public boolean get(long bitIndex) {
        try {
//            long byteIndex =bitIndex >>> 3;
//            raf.seek(byteIndex);
//            int b = raf.read();
//
//            int mask = 1 << (bitIndex & 0x07);
//            return (b & mask) != 0;

            if (bitIndex >= bitCount)
                return false;

            long byteIndex =bitIndex >>> 3;
            int chunkIndex = (int)(byteIndex / CHUNK_SIZE);
            mapChunk(chunkIndex);
            int b = map.get((int)(byteIndex & CHUNK_MASK));
            int mask = 1 << (bitIndex & 0x07);
            return (b & mask) != 0;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString(), e);
        }

    }

    public long size() {
        return bitCount;
    }

    public long nextClearBit(long bitIndex) {
        while (get(bitIndex) && bitIndex < bitCount)
            ++bitIndex;
        return bitIndex;
    }

    public long previousClearBit(long bitIndex) {
        while (get(bitIndex) && bitIndex >= 0)
            --bitIndex;
        return bitIndex;
    }

    public void close() {
        if (mapMode != FileChannel.MapMode.READ_ONLY)
            map.force();
    }
}
