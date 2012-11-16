/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

/**
 * A derived buffer which forbids any write requests to its parent.  It is
 * recommended to use {@link Unpooled#unmodifiableBuffer(ByteBuf)}
 * instead of calling the constructor explicitly.
 */
public class ReadOnlyByteBuf extends AbstractWrappedByteBuf {

    private final ByteBuf buffer;

    private Unsafe unsafe;

    public ReadOnlyByteBuf(ByteBuf buffer) {
        super(buffer.order(), buffer.maxCapacity());
        this.buffer = buffer;
        setIndex(buffer.readerIndex(), buffer.writerIndex());
    }

    private ReadOnlyByteBuf(ReadOnlyByteBuf buffer) {
        super(buffer.buffer.order(), buffer.maxCapacity());
        this.buffer = buffer.buffer;
        setIndex(buffer.readerIndex(), buffer.writerIndex());
    }

    @Override
    public ByteBuf unwrap() {
        return buffer;
    }

    @Override
    public ByteBufPool pool() {
        return buffer.pool();
    }

    @Override
    public boolean isDirect() {
        return buffer.isDirect();
    }

    @Override
    public boolean hasArray() {
        return false;
    }

    @Override
    public byte[] array() {
        throw new ReadOnlyBufferException();
    }

    @Override
    public int arrayOffset() {
        throw new ReadOnlyBufferException();
    }

    @Override
    public WrappedByteBuf discardReadBytes() {
        throw new ReadOnlyBufferException();
    }

    @Override
    public WrappedByteBuf setByte(int index, int value) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public WrappedByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public WrappedByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public WrappedByteBuf setBytes(int index, ByteBuffer src) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public WrappedByteBuf setShort(int index, int value) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public WrappedByteBuf setMedium(int index, int value) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public WrappedByteBuf setInt(int index, int value) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public WrappedByteBuf setLong(int index, long value) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public int setBytes(int index, InputStream in, int length)
            throws IOException {
        throw new ReadOnlyBufferException();
    }

    @Override
    public int setBytes(int index, ScatteringByteChannel in, int length)
            throws IOException {
        throw new ReadOnlyBufferException();
    }

    @Override
    public int getBytes(int index, GatheringByteChannel out, int length)
            throws IOException {
        return buffer.getBytes(index, out, length);
    }

    @Override
    public WrappedByteBuf getBytes(int index, OutputStream out, int length)
            throws IOException {
        buffer.getBytes(index, out, length);
        return this;
    }

    @Override
    public WrappedByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        buffer.getBytes(index, dst, dstIndex, length);
        return this;
    }

    @Override
    public WrappedByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        buffer.getBytes(index, dst, dstIndex, length);
        return this;
    }

    @Override
    public WrappedByteBuf getBytes(int index, ByteBuffer dst) {
        buffer.getBytes(index, dst);
        return this;
    }

    @Override
    public ByteBuf duplicate() {
        return new ReadOnlyByteBuf(this);
    }

    @Override
    public ByteBuf copy(int index, int length) {
        return buffer.copy(index, length);
    }

    @Override
    public ByteBuf slice(int index, int length) {
        return new ReadOnlyByteBuf(buffer.slice(index, length));
    }

    @Override
    public byte getByte(int index) {
        return buffer.getByte(index);
    }

    @Override
    public short getShort(int index) {
        return buffer.getShort(index);
    }

    @Override
    public int getUnsignedMedium(int index) {
        return buffer.getUnsignedMedium(index);
    }

    @Override
    public int getInt(int index) {
        return buffer.getInt(index);
    }

    @Override
    public long getLong(int index) {
        return buffer.getLong(index);
    }

    @Override
    public boolean hasNioBuffer() {
        return buffer.hasNioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int index, int length) {
        return buffer.nioBuffer(index, length).asReadOnlyBuffer();
    }

    @Override
    public boolean hasNioBuffers() {
        return buffer.hasNioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int offset, int length) {
        return buffer.nioBuffers(offset, length);
    }

    @Override
    public int capacity() {
        return buffer.capacity();
    }

    @Override
    public WrappedByteBuf capacity(int newCapacity) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public Unsafe unsafe() {
        Unsafe unsafe = this.unsafe;
        if (unsafe == null) {
            this.unsafe = unsafe = new ReadOnlyUnsafe();
        }
        return unsafe;
    }

    private final class ReadOnlyUnsafe implements Unsafe {

        @Override
        public ByteBuffer nioBuffer() {
            return buffer.unsafe().nioBuffer();
        }

        @Override
        public ByteBuffer[] nioBuffers() {
            return buffer.unsafe().nioBuffers();
        }

        @Override
        public ByteBuf newBuffer(int initialCapacity) {
            return buffer.unsafe().newBuffer(initialCapacity);
        }

        @Override
        public void discardSomeReadBytes() {
            throw new UnsupportedOperationException();
        }
    }
}
