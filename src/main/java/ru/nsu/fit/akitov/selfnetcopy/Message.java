package ru.nsu.fit.akitov.selfnetcopy;

public enum Message {
    ENTER, EXIT;

    public byte[] toByteArray() {
        return this.name().getBytes();
    }

    public static Message fromByteArray(byte[] bytes, int offset, int length) {
        return Message.valueOf(new String(bytes, offset, length));
    }
}
