package com.focess.betterai.utils.command;

public abstract class DataConverter<T> {

    public static final DataConverter<String> DEFAULT_DATA_CONVERTER = new DataConverter<String>() {
        @Override
        boolean accept(String arg) {
            return true;
        }

        @Override
        String convert(String arg) {
            return arg;
        }

        @Override
        void connect(DataCollection dataCollection, String arg) {
            dataCollection.write(arg);
        }
    };

    abstract boolean accept(String arg);

    abstract T convert(String arg);

    boolean put(DataCollection dataCollection, String arg) {
        if (this.accept(arg))
            this.connect(dataCollection,convert(arg));
        else return false;
        return true;
    }

    abstract void connect(DataCollection dataCollection,T arg);
}
