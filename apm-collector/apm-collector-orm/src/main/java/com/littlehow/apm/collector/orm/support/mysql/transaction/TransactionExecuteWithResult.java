package com.littlehow.apm.collector.orm.support.mysql.transaction;

public interface TransactionExecuteWithResult<T> {
    T run();
}
