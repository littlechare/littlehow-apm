package com.littlehow.apm.collector.orm.support.mysql.transaction;

import com.littlehow.apm.base.exception.ApmBizException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;

@Component
@Getter
@Slf4j
public class TransactionSupport {
    @Autowired
    @Qualifier("txRequired")
    private TransactionTemplate transactionTemplate;

    @Autowired
    @Qualifier("txReadCommit")
    private TransactionTemplate readCommitTransactionTemplate;

    public final void execute(final TransactionExecute execute) {
        execute(transactionTemplate, execute);
    }

    public final void executeReadCommit(final TransactionExecute execute) {
        execute(readCommitTransactionTemplate, execute);
    }

    public final <T> T executeWithResult(final TransactionExecuteWithResult<T> execute) {
        return executeWithResult(transactionTemplate, execute);
    }

    public final <T> T executeWithResultReadCommit(final TransactionExecuteWithResult<T> execute) {
        return executeWithResult(readCommitTransactionTemplate, execute);
    }

    /**
     * 执行带结果的事务
     *
     * @param template
     * @param execute
     * @return
     */
    private <T> T executeWithResult(TransactionTemplate template, TransactionExecuteWithResult<T> execute) {
        startTrace();
        T result = template.execute((status) -> {
            try {
                return execute.run();
            } catch (ApmBizException e) {
                log.info("ApmBizException or ApmAssertException caught in transaction : {}", e.getMessage());
                status.setRollbackOnly();
                throw e;
            } catch (DuplicateKeyException e) {
                log.info("DuplicateKeyException withTransaction exception : {}", e.getMessage());
                status.setRollbackOnly();
                throw e;
            } catch (Throwable e) {
                log.error("Throwable withTransaction exception : {}", e.getMessage(), e);
                status.setRollbackOnly();
                throw new RuntimeException(e);
            }
        });
        endTrace();
        return result;
    }

    /**
     * 执行不带结果的事务
     *
     * @param template
     * @param execute
     * @return
     */
    private void execute(TransactionTemplate template, TransactionExecute execute) {
        startTrace();
        template.execute((status) -> {
            try {
                execute.run();
            } catch (ApmBizException e) {
                log.info("ApmBizException or ApmAssertException caught in transaction : {}", e.getMessage());
                status.setRollbackOnly();
                throw e;
            } catch (DuplicateKeyException e) {
                log.info("DuplicateKeyException withTransaction exception : {}", e.getMessage());
                status.setRollbackOnly();
                throw e;
            } catch (Throwable e) {
                log.error("Throwable withTransaction exception : {}", e.getMessage(), e);
                status.setRollbackOnly();
                throw new RuntimeException(e);
            }
            return status;
        });
        endTrace();
    }

    private void startTrace() {
        MDC.put("txID", "txID-" + UUID.randomUUID().toString());
    }

    private void endTrace() {
        MDC.remove("txID");
    }
}
