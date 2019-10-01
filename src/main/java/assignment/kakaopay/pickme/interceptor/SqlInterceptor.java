package assignment.kakaopay.pickme.interceptor;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQL실행시 로그 롹인 밑 데이터 검증 인터셉트
 * @author 나현기
 *
 */
@Intercepts({
	@Signature(type = StatementHandler.class, method = "query",  args = {Statement.class, ResultHandler.class})
	, @Signature(type = StatementHandler.class, method = "update", args = {Statement.class})
})
public class SqlInterceptor implements Interceptor {
	
	private static Logger logger = LoggerFactory.getLogger(SqlInterceptor.class);
	
	private Field proxyMappedStatement;
	private Field proxyDelegate;
	
	public SqlInterceptor() {
		try {
			proxyMappedStatement = BaseStatementHandler.class.getDeclaredField("mappedStatement");
			proxyMappedStatement.setAccessible(true);
			proxyDelegate = RoutingStatementHandler.class.getDeclaredField("delegate");
			proxyDelegate.setAccessible(true);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Object intercept(Invocation invocation) throws Throwable {
		logger.trace("sql statement logging");
		
		Object result = null;
		int resultCount = 0;
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		PreparedStatementHandler preparedStatement = (PreparedStatementHandler) proxyDelegate.get(statementHandler);
		MappedStatement mappedStatement = (MappedStatement) proxyMappedStatement.get(preparedStatement);
		String sqlmapId = mappedStatement.getId();
		
		BoundSql boundSql = statementHandler.getBoundSql();
		String parameterMappedSql = boundSql.getSql();
		Object parameterObject = statementHandler.getParameterHandler().getParameterObject();
		Configuration configuration = mappedStatement.getConfiguration();
		List<Object> entryParamsValueList = getParameters(configuration, boundSql, parameterObject);
		
		String strValue = "";

		try {
			if(parameterObject == null) {
				// case1) 파라미터가 없는 경우
				parameterMappedSql = parameterMappedSql.replaceAll("\\?", "''");
			} else if(parameterObject instanceof Map) {
				// case2) 파라미터의 객체 타입이 Map인 경우
				List<ParameterMapping> entryParamKeyList = boundSql.getParameterMappings();
				if(entryParamsValueList.size() != entryParamKeyList.size()) {
					logger.error("[{}]SQLMAP에 정의된 파라미터가 요청된 파라미터의 개수와 일치하지 않습니다.", sqlmapId);
					return null;
				}
				Map<String, Object> entryParamMap = new HashMap<String, Object>();
				for(int i=0; i<entryParamKeyList.size(); i++) {
					Object value = entryParamsValueList.get(i);
					if(value == null) {
						continue;
					}
					strValue = value.toString();
					if(value instanceof String) {
						strValue = String.format("'%s'", strValue);
						parameterMappedSql = Pattern.compile("\\?").matcher(parameterMappedSql).replaceFirst(strValue);
					} else {
						parameterMappedSql = parameterMappedSql.replaceFirst("\\?", strValue);
					}
					entryParamMap.put(entryParamKeyList.get(i).getProperty(), entryParamsValueList.get(i));
				}
				parameterObject = entryParamMap.toString();
			} else if(parameterObject instanceof String) {
				// case3) 파라미터의 객체 타입이 String인 경우
				List<ParameterMapping> paramMapping = boundSql.getParameterMappings();
				strValue = String.format("'%s'", strValue);
				parameterMappedSql = Pattern.compile("\\?").matcher(parameterMappedSql).replaceFirst(strValue);
			} else {
				// case4) 파라미터 타입이 사용자 정의 클래스인 경우
				List<ParameterMapping> entryParamKeyList = boundSql.getParameterMappings();
				if(entryParamsValueList.size() != entryParamKeyList.size()) {
					logger.error("[{}]SQLMAP에 정의된 파라미터가 요청된 파라미터의 개수와 일치하지 않습니다.", sqlmapId);
					return null;
				}
				Class<? extends Object> parameterClass = parameterObject.getClass();
				for(ParameterMapping mapping : entryParamKeyList) {
					String propVal = mapping.getProperty();
					Field field = null;
					try {
						field = parameterClass.getDeclaredField(propVal);
					} catch(NoSuchFieldException e) {
						field = parameterClass.getField(propVal);
					}
					field.setAccessible(true);
					Class<?> javaType = mapping.getJavaType();
					if(String.class == javaType) {
						strValue = String.format("'%s'", field.get(parameterObject));
						parameterMappedSql = Pattern.compile("\\?").matcher(parameterMappedSql).replaceFirst(strValue);
					} else {
						strValue = String.format("%s", field.get(parameterObject));
						parameterMappedSql = Pattern.compile("\\?").matcher(parameterMappedSql).replaceFirst(strValue);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		parameterMappedSql = parameterMappedSql.replaceAll("\n\t\t", "\n");
		logger.info("[{}]\n{}", sqlmapId, parameterMappedSql);
		
		try {
			result = invocation.proceed();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Object> getParameters(Configuration configuration, BoundSql boundSql, Object mehtodParam) {
		List paramList = new ArrayList<Object>();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value = null;
					String propertyName = parameterMapping.getProperty();
					if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else {
						MetaObject metaObject = configuration.newMetaObject(mehtodParam);
						value = metaObject.getValue(propertyName);
					}
					paramList.add(value);
				}
			}
		}
		return paramList;
	}
	
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
	
	public void setProperties(Properties properties) {
	}
}