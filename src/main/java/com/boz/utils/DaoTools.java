package com.boz.utils;


import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DaoTools{
    private static final Logger log = LoggerFactory.getLogger(DaoTools.class);
    private static DataSource DATA_SOURCE=null;
	
	//public static DBPool  connPool= null;
    public static boolean initialized = false;
    
    public DaoTools(){
		init();
	}
	
    /*DBPool initialization*/
	public void init(){
		if(!initialized){
			//根据class文件位置判断当前是后台程序还是web环境
			log.info("---- START INIT DATASOURCE NOW !  ----");
			try{
				Properties dbconfig=DbConfig.getDBProperty();
				log.info("[DATASOURCE    DRIVERCLASSNAME]:"+dbconfig.getProperty("driverClassName"));
				log.info("[DATASOURCE    URL            ]:"+dbconfig.getProperty("url"));
				log.info("[DATASOURCE    USERNAME       ]:"+dbconfig.getProperty("username"));
				log.info("[DATASOURCE    PASSWORD       ]:"+dbconfig.getProperty("password"));
				DATA_SOURCE = DruidDataSourceFactory.createDataSource(dbconfig);

		    	log.debug("[DB_INIT]:  NOW DB INITED !  [DB_INIT]");
		    }catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
		    	log.error(" connectpool init fail! message is :"+e.getMessage());
				e.printStackTrace();
			}catch (Exception e){
		        // TODO Auto-generated catch block
		    	log.error("[DB_INIT Fail]: connectpool init Fail! message is :"+e.getMessage()+" [DB_INIT Fail]");
		        e.printStackTrace();
		    }
	    	initialized = true;
		}
   }
	
	/**
	 * 获取数据库连接，如果在WEB容器中，则从容器环境获取，否则直接从连接池中提取。
	 */
	public Connection getConnection(){
		//如果未释放则释放
		Connection conn=null;
		log.debug("GET CONNECT FROM DaoUtil !");

		//使用proxool连接池
		log.debug("GET Connect from Pool !!");
        try {
           	if (!initialized){
           		init();
            }
           	conn=DATA_SOURCE.getConnection();
           	
        }catch(Exception e){
	    	log.error(" get Connect from proxool ERROR :"+e.getMessage());
	        e.printStackTrace();
        }
		try {
			if(conn ==null){
				log.error("[ERROR] getConnect ERROR: CAN NOT GET CONNECT !!");
			}else if(conn.isClosed()){
				log.error("[ERROR] getConnect ERROR: THE CONNECT IS CLOSE!!");
			}else{
				log.debug("GET CONNECT!");
				//显示连接池状态
//				if(Level.DEBUG.equals(log.getEffectiveLevel())){
//					showSnapshotInfo();
//				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.warn("GET DBPOOL STATUS ERROR:"+e.getMessage());
			//e.printStackTrace();
		}
		
		return conn;
	}
	
	/**
	 * 显示proxool 连接池 状态
	 */
	public void showSnapshotInfo(){
		
	}
	
	/**
	 *	单行查询|参数:sql|返回值:包含查询结果的字符串数组
	 *	@param sql 执行语句
	 *  @return Map以小写字母结果列为key，结果为value的Map对象
	 *	@author 苌黄林
	 */
	public  Map<String,String> getSingleData(String sql){
		if (StringTools.isEmpty(sql)) {return null;}
		log.debug("getSingleData SQL:"+sql);
		Connection conn = getConnection();
		Statement statement = null ;
		ResultSet rs = null ;
		ResultSetMetaData rmd = null;
		Map resmap = null;
		try{
		if(conn==null){log.error("Can't get connection!");return null;}
			statement = conn.createStatement();
		    rs = statement.executeQuery(sql) ;
		    if(rs.next()){
				rmd=rs.getMetaData();
				resmap = new HashMap<String,String>();
				int j;
				int Count=rmd.getColumnCount();
				for(j=1;j<=Count;j++){
					//String colname = rmd.getColumnName(j).toLowerCase();//实际名称
					String collab = rmd.getColumnLabel(j).toLowerCase();//建议名,as后的名称，默认与colname一样
					String coltypename = rmd.getColumnTypeName(j).toLowerCase();
					if(coltypename.startsWith("timestamp") || coltypename.startsWith("date")){
						try{
							if(rs.getDate(collab)!=null){
								resmap.put(collab, DateTools.parseDate(new Date(rs.getTimestamp(collab).getTime()),"yyyy-MM-dd HH:mm:ss"));
							}
						}catch(Exception e){
							log.warn("GET TIMETYPE COLUMN DATA FAILE ,MESSAGE IS : "+e.getMessage(),e);
							resmap.put(collab, rs.getString(collab));
						}
					}else{
						resmap.put(collab, rs.getString(collab));
					}
					coltypename = null;
				}
		    }else{
		    	return null;
		    }
		}catch(Exception e){
			log.error("SQL:"+sql+"; getSingleData Exception!"+e.getMessage(),e);
			e.printStackTrace();
		}finally{
		    try {
		    	if(rs!=null){
		    		rs.close();
		    	}
	    		rs=null;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    try {
		    	if(statement!=null){
		    		statement.close();
		    	}
				statement=null;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(conn!=null){
					conn.close();
				}
				conn = null;
			} catch (SQLException e) {
				log.error("Connection Close Exception!");
				e.printStackTrace();
			}
		}
		return resmap;
	}
	
	/**
	 * 根据sql(必须为单结果count语句)查询总数
	 * @param sql
	 * @return
	 */
	public Integer getCountData(String sql){
		if(StringTools.isEmpty(sql) || sql.indexOf("count(")<0){
			return null;
		}
		log.debug("getCountData SQL:"+sql);
		Connection conn = getConnection();
		Statement statement = null ;
		ResultSet rs = null ;
		ResultSetMetaData rmd = null;
		Integer countresult = null;
		try{
			if(conn==null){log.error("Can't get connection!");return null;}
				statement = conn.createStatement();
			    rs = statement.executeQuery(sql) ;
			    System.out.println(rs.toString());
			    if(rs.next()){
			    	countresult = rs.getInt(1);
			    }else{
			    	return null;
			    }
			}catch(Exception e){
				log.error("SQL:"+sql+";getCountData Exception!"+e.getMessage(),e);
				e.printStackTrace();
			}finally{
			    try {
			    	if(rs!=null){
			    		rs.close();
			    	}
		    		rs=null;
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    try {
			    	if(statement!=null){
			    		statement.close();
			    	}
					statement=null;
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					if(conn!=null){
						conn.close();
					}
					conn = null;
				} catch (SQLException e) {
					log.error("Connection Close Exception!");
					e.printStackTrace();
				}
			}
			return countresult;
	}
	
	/**
	 *	多行查询|参数:sql;返回值:包含查询结果的包含多行查询结果的
	 *	@param sql 执行语句
	 *  @return ArrayList<Map>以小写字母结果列为key，结果为value的Map对象List
	 *	@author 苌黄林
	 */
	public  List<Map<String,String>> getMultiData(String sql){
		ArrayList resultlist = null;
		if (StringTools.isEmpty(sql)){
			return null;
		}
		log.debug("getMultiData SQL:"+sql);
		Connection conn = getConnection();
		Statement statement = null ;
		ResultSet rs = null ;
		ResultSetMetaData rmd = null;
		try{
		if(conn==null){log.error("Can't get connection!");return null;}
			statement = conn.createStatement();
		    rs = statement.executeQuery(sql) ;
			rmd=rs.getMetaData();
			int Count=rmd.getColumnCount();
		    while(rs.next()){
		    	if(resultlist==null){
		    		resultlist = new ArrayList();
		    	}
				Map resmap = new HashMap<String,String>();
				for(int j=1;j<=Count;j++){
					//String colname = rmd.getColumnName(j).toLowerCase();//实际名称
					String collab = rmd.getColumnLabel(j).toLowerCase();//建议名,as后的名称，默认与colname一样
					String coltypename = rmd.getColumnTypeName(j).toLowerCase();
					if(coltypename.startsWith("timestamp") || coltypename.startsWith("date")){
						try{
							if(rs.getDate(collab)!=null){
								resmap.put(collab, DateTools.parseDate(new Date(rs.getTimestamp(collab).getTime()),"yyyy-MM-dd HH:mm:ss"));
							}
						}catch(Exception e){
							log.warn("GET TIMETYPE COLUMN DATA FAILE ,MESSAGE IS : "+e.getMessage(),e);
							resmap.put(collab, rs.getString(collab));
						}
					}else{
						resmap.put(collab, rs.getString(collab));
					}
					coltypename = null;
				}
				resultlist.add(resmap);
		    }
		}catch(Exception e){
			log.error("SQL:"+sql+";getMultiData Exception!"+e.getMessage(),e);
			e.printStackTrace();
		}finally{
		    try {
		    	if(rs!=null){
		    		rs.close();
		    	}
	    		rs=null;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    try {
		    	if(statement!=null){
		    		statement.close();
		    	}
				statement=null;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(conn!=null){
					conn.close();
				}
				conn = null;
			} catch (SQLException e) {
				log.error("Connection Close Exception!");
				e.printStackTrace();
			}
		}
		return resultlist;
	}
	
	/**
	 * 执行对数据库的更新操作,自动事务提交
	 * @param sql		语句sql(UPDATE,DELETE,INSERT)
	 * @return boolean	更新成功为true;更新失败为false
	 */
	public boolean ExecSQL(String sql){
		if (StringTools.isEmpty(sql)) return false;
		log.debug("Exec SQL:"+sql);
		boolean bool=false;
		Connection conn = getConnection();
		Statement statement = null ;
        if(conn==null){
        	log.error("Can't get connection!");
        	return false;
        }
		try{
			//conn.setAutoCommit(false);
			statement = conn.createStatement();
			statement.execute(sql);
			statement.close();
            bool=true;
			//conn.commit();
		}catch(Exception e){
			log.error("SQL:"+sql+"; exec Exception Message is :"+e.getMessage());
			e.printStackTrace();
            try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error(" SQL exec RollBack ERROR!!!  Exception Message is :"+e.getMessage());
				e1.printStackTrace();
			}
        }finally{
        	try {
		    	if(statement!=null){
		    		statement.close();
		    	}
				statement=null;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(conn!=null){
					conn.close();
				}
				conn = null;
			} catch (SQLException e) {
				log.error("Connection Close Exception!");
				e.printStackTrace();
			}
        }
        return bool;
	}

    /**
     * 带参数的数据库批处理执行方法
     * @param sql
     * @param paramValues	List<Object[]> 每个List的Object[]为一套参数，每个Object[]为每次执行sql的参数，顺序与数量必须与语句中?对应位置一致
     * @return
     * @throws SQLException
     */
    public boolean executePreparedSQL(String sql, List<Object[]> paramValues) throws SQLException {
    	Connection conn = getConnection();
    	if(paramValues==null || paramValues.size()==0 || StringTools.isEmpty(sql)){
    		return false;
    	}
    	try{
	        java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
	        log.debug("Will  execute Prepared SQL:"+sql);
	        for (Object[] vol:paramValues) {
	        	int voi = 1;
	        	for(Object param : vol){
	        		if (param instanceof Integer) {
	        		    int value = ((Integer) param).intValue();
	        		    pstmt.setInt(voi, value);
	        		   } else if (param instanceof String) {
	        		    String s = (String) param;
	        		    pstmt.setString(voi, s);
	        		   } else if (param instanceof Double) {
	        		    double d = ((Double) param).doubleValue();
	        		    pstmt.setDouble(voi, d);
	        		   } else if (param instanceof Float) {
	        		    float f = ((Float) param).floatValue();
	        		    pstmt.setFloat(voi, f);
	        		   } else if (param instanceof Long) {
	        		    long l = ((Long) param).longValue();
	        		    pstmt.setLong(voi, l);
	        		   } else if (param instanceof Boolean) {
	        		    boolean b = ((Boolean) param).booleanValue();
	        		    pstmt.setBoolean(voi, b);
	        		   } else if (param instanceof java.util.Date) {
	        			java.util.Date d = (java.util.Date) param;
	        			pstmt.setDate(voi, new java.sql.Date(d.getTime()));
	        		   }else{
	        			   pstmt.setObject(voi, param);
	        		   }
	        		voi++;
	        	}
	        	pstmt.addBatch();
	        }
	        pstmt.executeBatch();
	        pstmt.close();
		}catch(Exception e){
			log.error("SQL:"+sql+"; exec Exception Message is :"+e.getMessage());
			e.printStackTrace();
	        try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error(" SQL exec RollBack ERROR!!!  Exception Message is :"+e.getMessage());
				e1.printStackTrace();
			}
	    }finally{
			try {
				if(conn!=null){
					conn.close();
				}
				conn = null;
			} catch (SQLException e) {
				log.error("Connection Close Exception!");
				e.printStackTrace();
			}
	    }
        return true;
    }
	/**
     * 在一个事务内处理多个sql语句，若其中有不成功的语句，则整个事务回滚
     * @param c Collection:要执行的sql语句的字符串的集合，其中每个元素是一个String型对象
     * @return boolean：成功返回 true，失败（回滚）返回 false
     * @throws SQLException
     * 由于该部分不需要返回结果集，所以执行完毕即刻关闭语句、连接
     */
    public boolean executeBatchSQL(Collection c){
    	Connection conn = getConnection();
        boolean result = false;
        boolean autoCommit = false;
        Iterator ir = c.iterator();
        Statement stmt = null;
        String sql = null;
        try {

            autoCommit = conn.getAutoCommit(); //得到当前是否自动执行状态
            conn.setAutoCommit(false); //设当前自动执行状态为否
            stmt = conn.createStatement();

            while (ir.hasNext()) {
                sql = (String) (ir.next());
        		log.debug("Will Batch exec SQL:"+sql);
                stmt.addBatch(sql);
            }
            try {
                stmt.executeBatch();
                conn.commit();
                conn.setAutoCommit(autoCommit);
            }catch (BatchUpdateException buex) {
                buex.printStackTrace();
                conn.rollback();
                conn.setAutoCommit(autoCommit); //恢复执行前自动执行状态
                stmt.clearBatch();
                log.error("SQL:"+sql+"; Exception with executebatchTask ：" + buex.getMessage() + sql);
            }
            result = true;
        }catch (Exception ex) {
            log.error("Exception with executebatchTask ：" + ex.getMessage());
            ex.printStackTrace();
        }finally {
		    try {
		    	if(stmt!=null){
		    		stmt.close();
		    	}
		    	stmt=null;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(conn!=null){
					conn.close();
				}
				conn = null;
			} catch (SQLException e) {
				log.error("Connection Close Exception!");
				e.printStackTrace();
			}
        }
        return result;
    }
    
	/**
	 * 获取CLOB值，进查询某行的一个CLOB列，返回数据字符串内容
	 * @param sql
	 * @return
	 */
    public String getClobValue(String sql){
    	if(StringTools.isEmpty(sql)){
    		return null;
    	}
    	log.debug("getClob SQL:"+sql);
		Connection conn = getConnection();
		Statement statement = null ;
		ResultSet rs = null ;
		ResultSetMetaData rmd = null;
		String resmap = null;
		if(conn==null){log.error("Can't get connection!");return null;}
		try{
			statement = conn.createStatement();
		    rs = statement.executeQuery(sql) ;
		    if(rs.next()){
		    	java.sql.Clob c = (Clob)rs.getClob(1);
			    Reader reader=c.getCharacterStream();
				if (reader == null) { 
					return null; 
				} 
				StringBuffer sb = new StringBuffer(); 
				char[] charbuf = new char[4096]; 
				for (int i = reader.read(charbuf); i > 0; i = reader.read(charbuf)) { 
					sb.append(charbuf, 0, i); 
				}
				return sb.toString(); 
		    } 
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		    try {
		    	if(rs!=null){
		    		rs.close();
		    	}
	    		rs=null;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    try {
		    	if(statement!=null){
		    		statement.close();
		    	}
				statement=null;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(conn!=null){
					conn.close();
				}
				conn = null;
			} catch (SQLException e) {
				log.error("Connection Close Exception!");
				e.printStackTrace();
			}
		}
		return resmap;
    	
    }
	
    /**
	 * 获取BLOB值，进查询某行的一个BLOB列，返回数据流
	 * @param sql
	 * @return
	 */
    public InputStream getBlobValue(String sql){
    	if(StringTools.isEmpty(sql)){
    		return null;
    	}
    	log.debug("getBlob SQL:"+sql);
		Connection conn = getConnection();
		Statement statement = null ;
		ResultSet rs = null ;
		ResultSetMetaData rmd = null;
		if(conn==null){
			log.error("Can't get connection!");return null;
		}
		try{
			statement = conn.createStatement();
		    rs = statement.executeQuery(sql) ;
		    if(rs.next()){
		    	java.sql.Blob blob = rs.getBlob(1);
		        return blob.getBinaryStream();
		    } 
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		    try {
		    	if(rs!=null){
		    		rs.close();
		    	}
	    		rs=null;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    try {
		    	if(statement!=null){
		    		statement.close();
		    	}
				statement=null;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(conn!=null){
					conn.close();
				}
				conn = null;
			} catch (SQLException e) {
				log.error("Connection Close Exception!");
				e.printStackTrace();
			}
		}
		return null;
    }
    
	/**
     * map反射写入obj对象属性,反射时属性名不区分大小写
     * 
     * @param pmap
     * @param obj
     */
    public static void injectionObjectFromMap(Map<String,String> pmap,Object obj){
    	if (obj == null || pmap ==null) {
    		obj=null;
    		return;
        }
    	//判断属性参数类型后、反射注入
    	for (Iterator<String> iterator = pmap.keySet().iterator(); iterator.hasNext();) {
             String key = iterator.next();
             String value = pmap.get(key);
             if(value==null || StringTools.isEmpty(value)){
            	 continue;
             }
             Method[] methods = obj.getClass().getMethods();
             for (Method m : methods) {
            	 //获取setkey方法(不区分大小写)，全部做LowerCase转化
                 String mname = "set" + key;
                 if (mname.equals(m.getName().toLowerCase())) {
                     try {
                    	 String paramtype=m.getParameterTypes()[0].getName();
                    	 if(paramtype.equals("java.lang.String")){
                             m.invoke(obj, new Object[]{value});
                    	 }else if(paramtype.equals("java.lang.Integer") || paramtype.equals("int")){
                    		 m.invoke(obj, new Object[]{Integer.parseInt(value)});
                    	 }else if(paramtype.equals("java.lang.Long") || paramtype.equals("long")){
                    		 m.invoke(obj, new Object[]{Long.parseLong(value)});
                    	 }else if(paramtype.equals("java.lang.Double") || paramtype.equals("double")){
                    		 m.invoke(obj, new Object[]{Double.parseDouble(value)});
                    	 }else if(paramtype.equals("java.lang.Float") || paramtype.equals("float")){
                    		 m.invoke(obj, new Object[]{Float.parseFloat(value)});
                    	 }else if(paramtype.equals("java.lang.Boolean") || paramtype.equals("boolean")){
                    		 m.invoke(obj, new Object[]{Boolean.parseBoolean(value)});
                    	 }else if(paramtype.equals("java.util.Date")){
                    		 if(DateTools.parseDate(value)!=null){
                        		 m.invoke(obj, new Object[]{DateTools.parseDate(value)}); 
                    		 }
                    	 }
                     } catch (SecurityException e) {
                    	 log.warn(key+"反射注入'"+value+"'异常!:"+e.getMessage());
                     } catch (IllegalArgumentException e) {
                    	 log.warn(key+"反射注入'"+value+"'异常!:"+e.getMessage());
                     } catch (IllegalAccessException e) {
                    	 log.warn(key+"反射注入'"+value+"'异常!:"+e.getMessage());
                     } catch (InvocationTargetException e) {
                    	 log.warn(key+"反射注入'"+value+"'异常!:"+e.getMessage());
                     } catch (Exception e) {
                    	 log.warn(key+"反射注入'"+value+"'异常!:"+e.getMessage());
                     }
                 }
             }
         }
    }
    
    public static void main(String[]  arg){
    	DaoTools dao=new DaoTools();
//    	//查询单个值
//    	int i = dao.getCountData("select count(*) from PLS_USER_LOGIN_LOG");
//    	System.out.println("PLS_USER_LOGIN_LOG count ="+i);
//    	
//    	//查询单行值
//    	Map rm=dao.getSingleData("select * from PLS_USER_LOGIN_LOG  where PK_ID='10000637'");
//    	System.out.println(" mac = "+rm.get("mac")+";pk_id="+rm.get("pk_id"));
    	
    	
    	//查询多行数据
//    	List<Map<String, String>> lists = dao.getMultiData("select * from PLS_USER_LOGIN_LOG where username='张博'");
//    	for(Map<String,String> list: lists){
//    		System.out.println("pk_id="+list.get("pk_id")+";mac="+list.get("mac"));
//    	}
//    	
    	//更新数据
    	//dao.ExecSQL("update pls_user_login_log set mac=1 where pk_id=10001785");
    	//删除数据
    	//dao.ExecSQL("delete from pls_user_login_log where pk_id=10001785");
    	//插入数据
    	//dao.ExecSQL("insert into pls_user_login_log (pk_id,mac,account,username) values (pls_user_login_log_seq.nextval,'666','666','张博')");
    	/**
    	//批量插入
    	Object [] arr = new Object[]{"111","111","藏一"};
    	Object [] arr2 = new Object[]{"222","222","藏二"};
    	Object [] arr3 = new Object[]{"333","333","藏三"};
    	ArrayList<Object[]> paramValues = new ArrayList<Object[]>();
    	paramValues.add(arr);
    	paramValues.add(arr2);
    	paramValues.add(arr3);
    	try {
			dao.executePreparedSQL("insert into pls_user_login_log (pk_id,mac,account,username) values (pls_user_login_log_seq.nextval,?,?,?)", paramValues);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
    	//连接mysql(同oracle)
    	System.out.println(dao.getSingleData("SELECT * from t_user limit 1"));
    }
}
