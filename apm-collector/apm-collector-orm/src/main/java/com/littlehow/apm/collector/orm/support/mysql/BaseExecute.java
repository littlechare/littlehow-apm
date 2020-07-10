package com.littlehow.apm.collector.orm.support.mysql;

import com.littlehow.apm.collector.orm.support.mysql.transaction.TransactionSupport;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseExecute {
    @Autowired
    protected TransactionSupport transactionSupport;
}
