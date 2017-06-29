package com.tak.daocore.dao.impl.query.param;


import com.tak.daocore.dao.exception.QueryException;
import com.tak.daocore.dao.impl.query.QueryLogic;
import com.tak.daocore.dao.impl.query.QueryOperator;
import com.tak.daocore.utils.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Desc：描述模块内容
 * User: ZhouJing
 * Date: 2017/6/28 15:23
 */
@SuppressWarnings("all")
public class QueryParam implements Serializable {

    private static final long serialVersionUID = 1068494967017608389L;
    protected QueryLogic logic;
    protected String field;
    protected QueryOperator operator;
    protected Object value;
    protected List<QueryParam> andSubParams;
    protected List<QueryParam> orSubParams;
    protected Map<String, Object> paramValues;
    protected QueryParam parent;
    
	public QueryParam() {
        this.logic = QueryLogic.AND;
        this.andSubParams = new ArrayList();
        this.orSubParams = new ArrayList();
        this.paramValues = new HashMap();
    }

    public QueryParam(String field, QueryOperator operator, Object value) {
        this(QueryLogic.AND, field, operator, value);
    }

    public QueryParam(QueryLogic logic, String field, QueryOperator operator, Object value) {
        this.logic = QueryLogic.AND;
        this.andSubParams = new ArrayList();
        this.orSubParams = new ArrayList();
        this.paramValues = new HashMap();
        this.logic = logic;
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public boolean isEmpty() {
        return this.field == null && this.andSubParams.isEmpty() && this.orSubParams.isEmpty();
    }

    public String getQueryClause(boolean withLogic) throws QueryException {
        if (isEmpty()) {
            return "";
        } else {
            StringBuilder clause = this.getStringBuilder(withLogic);
            if (this.value != null && this.operator == QueryOperator.LIKE && this.value instanceof String && !String.valueOf(this.value).contains("%")) {
                this.value = "%" + this.value + "%";
            }
            clause.append(this.operator.getOpt()).append(this.getPlaceHolder(this.value));
            this.appendSubParams(clause);
            return clause.toString();
        }
    }

    public String getQueryClause() throws QueryException {
        return this.getQueryClause(false);
    }

    public Map<String, Object> getParamValues() {
        return this.paramValues;
    }

    public void andParam(QueryParam param) {
        Assert.notNull(param);
        if (this.isEmpty()) {
            BeanUtils.copyProperties(param, this);
        } else {
            param.setParent(this);
            this.andSubParams.add(param);
        }
    }

    public void orParam(QueryParam param) {
        Assert.notNull(param);
        if (this.isEmpty()) {
            BeanUtils.copyProperties(param, this);
        } else {
            param.setParent(this);
            this.orSubParams.add(param);
        }
    }

    public void setParent(QueryParam param) {
        this.parent = param;
        this.paramValues = param.getParamValues();
    }

    public QueryLogic getLogic() {
        return this.logic;
    }

    public void setLogic(QueryLogic logic) {
        this.logic = logic;
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public QueryOperator getOperator() {
        return this.operator;
    }

    public void setOperator(QueryOperator operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<QueryParam> getAndSubParams() {
        return this.andSubParams;
    }

    public void setAndSubParams(List<QueryParam> andSubParams) {
        this.andSubParams = andSubParams;
    }

    public List<QueryParam> getOrSubParams() {
        return this.orSubParams;
    }

    public void setOrSubParams(List<QueryParam> orSubParams) {
        this.orSubParams = orSubParams;
    }

    protected String getPlaceHolder(Object value) {
        String holder = this.field;
        if (holder.contains(".")) {
            holder = holder.replaceAll("\\.", "_");
        }

        int index = this.getParamCount(value);
        if (index > 0) {
            holder = holder + index;
        }

        this.paramValues.put(holder, value);
        return ":" + holder;
    }

    protected StringBuilder getStringBuilder(boolean withLogic) {
        Assert.notNull(this.field, "field不能为空");
        return (withLogic ? new StringBuilder(this.logic.getLogic()) : new StringBuilder()).append(this.field);
    }

    protected boolean isBlank(Object val) {
        return val == null || StringUtils.isBlank(val.toString());
    }

    protected void appendSubParams(StringBuilder clause) throws QueryException {
        this.appendSubParams(clause, QueryLogic.AND, this.andSubParams);
        this.appendSubParams(clause, QueryLogic.OR, this.orSubParams);
    }

    private void appendSubParams(StringBuilder clause, QueryLogic logic, List<QueryParam> params) throws QueryException {
        if (params.size() > 0) {
            clause.append(logic.getLogic());
            if (params.size() > 1) {
                clause.append("(");
            }

            boolean withFlag = false;
            Iterator i$ = params.iterator();

            while (i$.hasNext()) {
                QueryParam param = (QueryParam) i$.next();
                param.setParent(this);
                clause.append(param.getQueryClause(withFlag));
                if (!withFlag) {
                    withFlag = true;
                }
            }
            if (params.size() > 1) {
                clause.append(")");
            }
        }

    }

    private int getParamCount(Object value) {
        Assert.notNull(value);
        int i = 0;
        if (this.paramValues.containsKey(this.field) && !value.equals(this.paramValues.get(this.field))) {
            i = 1;
            while (true) {
                String key = this.field + i;
                if (!this.paramValues.containsKey(key) || value.equals(this.paramValues.get(key))) {
                    break;
                }
                ++i;
            }
        }
        return i;
    }

}
