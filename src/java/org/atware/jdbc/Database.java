/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Melarga.COULIBALY
 */
package org.atware.jdbc;

import java.sql.*;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.naming.InitialContext;
import org.atware.bean.BeanInfoFactory;
import org.atware.bean.Property;
import com.atware.utils.MySQLUtils;
import com.atware.utils.ResLoader;
import com.atware.utils.Utility;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.activation.DataSource;
import javax.naming.InitialContext;
import org.atware.bean.ConfigInfos;

/**
 *
 * @author melarga.coulibaly
 */
public class Database
{

    private Connection conn;
    private ResultSet rsltSet;
    private Statement statement;
    private PreparedStatement pstmt;
    private ResultSetMetaData rsltMetaData;
    private DatabaseMetaData dbMetaData;
    private String catalog;
    private String types[];
    private String message;
    private String url;
    private Properties connProperties;
    private static String mode = null;
    private static String strDataSource = null;
    private Integer rst;
    private MySQLUtils utils = new MySQLUtils(); 
    private Connection dbConnect = null;
    private Statement dbStatement = null;
    private ConfigInfos config = null;
    
    public Database()
    {
    }

    public Database(String driver)
    {
        try {
            config = new ConfigInfos();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        if(strDataSource != null)
        {
            return;
        }
        if(mode == null)
        {
            mode = config.getMode();
        }
        if(mode != null && mode.equalsIgnoreCase("Pool"))
        {
            strDataSource = config.getDatasource();
        } else
        {
            types = new String[1];
            types[0] = "TABLE";
            try
            {
                Class.forName(driver).newInstance();
            }
            catch(Exception e)
            {
                setMessage(e.getMessage());
            }
        }
    }

    public void openConnection(String url, String login, String password)
        throws Exception
    {
        try
        {
            setUrl(url);
            conn = DriverManager.getConnection(url, login, password);
            dbMetaData = conn.getMetaData();
            catalog = conn.getCatalog();
        }
        catch(Exception e)
        {
            setMessage(e.getMessage());
            throw e;
        }
    }

    public void openConnectionPool()
    {
        try
        {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(strDataSource);
            //Connection con = ds.getConnection();
            //conn = ds.getConnection(con);
           // conn = ds.getClass();
            dbMetaData = conn.getMetaData();
            catalog = conn.getCatalog();
        }
        catch(Exception ex)
        {
            setMessage(ex.getMessage());
        }
    }

    public void open(String url, String login, String password)
        throws Exception
    {
        if(mode != null && mode.equalsIgnoreCase("Pool"))
        {
            openConnectionPool();
           
        } else
        {
            openConnection(url, login, password);
        }
    }

    public void open(String url, Properties props)
    {
        setUrl(url);
        setConnProperties(props);
        try
        {
            conn = DriverManager.getConnection(url, props);
        }
        catch(Exception e)
        {
            setMessage(e.getMessage());
        }
    }

    public void close()
    {
        try
        {
            if(conn != null)
            {
                conn.close();
            }
        }
        catch(Exception e)
        {
            setMessage(e.getMessage());
        }
    }

    
    public String[] getColumnsNames(String tableName)
    {
        String clnames[] = null;
        Vector cname = new Vector();
        try
        {
            for(ResultSet rs = dbMetaData.getColumns(catalog, null, tableName, "%"); rs.next(); cname.addElement(rs.getString("COLUMN_NAME"))) { }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        clnames = new String[cname.size()];
        clnames = (String[])(String[])cname.toArray(clnames);
        return clnames;
    }

    public String[] getTableNames(String schema)
    {
        String tbnames[] = null;
        Vector tname = new Vector();
        try
        {
            for(ResultSet rs = dbMetaData.getTables(catalog, schema, "%", types); rs.next(); tname.addElement(rs.getString("TABLE_NAME"))) { }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        tbnames = new String[tname.size()];
        tbnames = (String[])(String[])tname.toArray(tbnames);
        return tbnames;
    }

    public PreparedStatement getPrepareStatement(String sql)
        throws SQLException
    {
        System.out.println((new StringBuilder()).append((new Date()).toString()).append(": ").append(sql).toString());
        try
        {
            pstmt = conn.prepareStatement(sql, 1005, 1008);
           
        }
        catch(Exception e)
        {
            String msg = e.getMessage();
            System.out.println((new StringBuilder()).append("Message BD: ").append(msg).toString());
            if(e instanceof NullPointerException)
            {
                setMessage("Database not available");
            } else
            {
                setMessage(msg);
            }
        }
        return pstmt;
    }
    public PreparedStatement getPreparedstatement(String sql)
        throws SQLException
    {
        System.out.println((new StringBuilder()).append((new Date()).toString()).append(": ").append(sql).toString());
        try
        {
            pstmt = conn.prepareStatement(sql);
           
        }
        catch(Exception e)
        {
            String msg = e.getMessage();
            System.out.println((new StringBuilder()).append("Message BD: ").append(msg).toString());
            if(e instanceof NullPointerException)
            {
                setMessage("Database not available");
            } else
            {
                setMessage(msg);
            }
        }
        return pstmt;
    }

    public ResultSet execute(String sql)
        throws SQLException
    {
        ResultSet rs = null;
        System.out.println((new StringBuilder()).append((new Date()).toString()).append(": ").append(sql).toString());
        try
        {
            pstmt = conn.prepareStatement(sql, 1005, 1008);
            rs = pstmt.executeQuery();
        }
        catch(Exception e)
        {
            String msg = e.getMessage();
            System.out.println((new StringBuilder()).append("Message BD: ").append(msg).toString());
            if(e instanceof NullPointerException)
            {
                setMessage("Database not available");
            } else
            {
                setMessage(msg);
            }
        }
        return rs;
    }


    public int ExecuteUpdate(String sql) throws SQLException {
        
        int result;
        pstmt = conn.prepareStatement(sql);
        result = pstmt.executeUpdate();
            
        return result;
    }
    
    public String Quote(String str) throws SQLException {
        
        String Escapstr= str;
        try {
            Escapstr = utils.mysql_real_escape_string(conn,str);
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Escapstr;        
    } 
    
    public ResultSet GetResult(String sql) throws SQLException, NullPointerException {
        ResultSet rs = null;
        statement = conn.createStatement();
        rs = statement.executeQuery(sql);

        return rs;
    }
    
    public int executeUpdate(String sql)
        throws SQLException
    {
        int result = 0;
        System.out.println((new StringBuilder()).append((new Date()).toString()).append(": ").append(sql).toString());
        try
        {
            pstmt = conn.prepareStatement(sql, 1005, 1008);
            result = pstmt.executeUpdate();
        }
        catch(Exception e)
        {
            String msg = e.getMessage();
            System.out.println((new StringBuilder()).append("Message BD: ").append(msg).toString());
            if(e instanceof NullPointerException)
            {
                setMessage("Database not available");
            } else
            {
                setMessage(msg);
            }
        }
        if(pstmt != null)
        {
            pstmt.close();
        }
        return result;
    }

    public boolean insertObjectAsRow(Object object)
    {
        return insertObjectAsRow(object, object.getClass().getSimpleName());
    }

    public boolean insertObjectAsRow(Object object, String table)
    {
        Property properties[] = BeanInfoFactory.getProperties(object);
        try
        {
            String sql = (new StringBuilder()).append("Select * from ").append(table).append(" where 1=2").toString();
            ResultSet rs = execute(sql);
            rs.moveToInsertRow();
            for(int i = 0; i < properties.length; i++)
            {
                if(properties[i].getPropertyName().compareToIgnoreCase("class") != 0)
                {
                    rs.updateObject(properties[i].getPropertyName(), properties[i].getPropertyValue());
                }
            }

            rs.insertRow();
            closeQuery(rs);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertObjectAsRowByQuery(Object object, String table)
    {
        Property properties[] = BeanInfoFactory.getProperties(object);
        Timestamp T = new java.sql.Timestamp(0);
        Date D = new java.sql.Date(0);
        String S = new java.lang.String();

        try
        {
            String sql = (new StringBuilder()).append("INSERT INTO ").append(table).append("(").toString();
            for(int i = 0; i < properties.length - 1; i++)
            {
                if(!properties[i].getPropertyName().equalsIgnoreCase("class") && properties[i].getPropertyValue() != null)
                {
                    sql = (new StringBuilder()).append(sql).append(properties[i].getPropertyName()).append(",").toString();
                }
            }

            if(!properties[properties.length - 1].getPropertyName().equalsIgnoreCase("class") && properties[properties.length - 1].getPropertyValue() != null)
            {
                sql = (new StringBuilder()).append(sql).append(properties[properties.length - 1].getPropertyName()).append(") VALUES (").toString();
            } else
            {
                sql = (new StringBuilder()).append(sql.substring(0, sql.length() - 1)).append(") VALUES (").toString();
            }
            String pfx = "";
            String sfx = "";
            for(int i = 0; i < properties.length - 1; i++)
            {
                if(properties[i].getPropertyName().equalsIgnoreCase("class") || properties[i].getPropertyValue() == null)
                {
                    continue;
                }
                if(properties[i].getPropertyValue().getClass().equals(S.getClass()))
                {
                    pfx = sfx = "'";
                    properties[i].setPropertyValue(properties[i].getPropertyValue().toString().replaceAll("'", "''"));
                }
                if(properties[i].getPropertyValue().getClass().equals(D.getClass()))
                {
                    pfx = ResLoader.getMessages("pfxDate");
                    sfx = ResLoader.getMessages("sfxDate");
                    properties[i].setPropertyValue(Utility.convertDateToString((java.sql.Date)properties[i].getPropertyValue(), ResLoader.getMessages("patternDate")));
                }
                if(properties[i].getPropertyValue().getClass().equals(T.getClass()))
                {
                    pfx = ResLoader.getMessages("pfxTimestamp");
                    sfx = ResLoader.getMessages("sfxTimestamp");
                    properties[i].setPropertyValue(Utility.convertDateToString((Date)properties[i].getPropertyValue(), ResLoader.getMessages("patternTimestamp")));
                }
                sql = (new StringBuilder()).append(sql).append(pfx).append(properties[i].getPropertyValue()).append(sfx).append(",").toString();
                pfx = "";
                sfx = "";
            }

            if(!properties[properties.length - 1].getPropertyName().equalsIgnoreCase("class") && properties[properties.length - 1].getPropertyValue() != null)
            {
                
                if(properties[properties.length - 1].getPropertyValue().getClass().equals(S.getClass()))
                {
                    pfx = sfx = "'";
                    properties[properties.length - 1].setPropertyValue(properties[properties.length - 1].getPropertyValue().toString().replaceAll("'", "''"));
                }
                if(properties[properties.length - 1].getPropertyValue().getClass().equals(D.getClass()))
                {
                    pfx = ResLoader.getMessages("pfxDate");
                    sfx = ResLoader.getMessages("sfxDate");
                    properties[properties.length - 1].setPropertyValue(Utility.convertDateToString((java.sql.Date)properties[properties.length - 1].getPropertyValue(), ResLoader.getMessages("patternDate")));
                }
                
                if(properties[properties.length - 1].getPropertyValue().getClass().equals(T.getClass()))
                {
                    pfx = ResLoader.getMessages("pfxTimestamp");
                    sfx = ResLoader.getMessages("sfxTimestamp");
                }
                sql = (new StringBuilder()).append(sql).append(pfx).append(properties[properties.length - 1].getPropertyValue()).append(sfx).append(")").toString();
            } else
            {
                sql = (new StringBuilder()).append(sql.substring(0, sql.length() - 1)).append(")").toString();
            }
            if(!sql.equals((new StringBuilder()).append("INSERT INTO ").append(table).append("(").toString()))
            {
                executeUpdate(sql);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
 
    public boolean updateRowByObjectByQuery(Object object, String table, String where)
    {
        Property properties[] = BeanInfoFactory.getProperties(object);
        String pfx = "";
        String sfx = "";
        Timestamp T = new java.sql.Timestamp(0);
        Date D = new java.sql.Date(0);
        String S = new java.lang.String();
        Float F = new java.lang.Float(0);

        try
        {
            String sql = (new StringBuilder()).append("UPDATE ").append(table).append(" SET ").toString();
            for(int i = 0; i < properties.length - 1; i++)
            {
                if(!properties[i].getPropertyName().equalsIgnoreCase("class") && properties[i].getPropertyValue() != null)
                {
                    if(properties[i].getPropertyValue().getClass().equals(S.getClass()))
                    {
                        pfx = sfx = "'";
                        properties[i].setPropertyValue(properties[i].getPropertyValue().toString().replaceAll("'", "''"));
                    }
                    if(properties[i].getPropertyValue().getClass().equals(D.getClass()))
                    {
                        pfx = ResLoader.getMessages("pfxDate");
                        sfx = ResLoader.getMessages("sfxDate");
                        properties[i].setPropertyValue(Utility.convertDateToString((java.sql.Date)properties[i].getPropertyValue(), ResLoader.getMessages("patternDate")));
                    }
                    if(properties[i].getPropertyValue().getClass().equals(T.getClass()))
                    {
                        properties[i].getPropertyValue().getClass().equals(F.getClass());
                        pfx = ResLoader.getMessages("pfxTimestamp");
                        sfx = ResLoader.getMessages("sfxTimestamp");
                        properties[i].setPropertyValue(Utility.convertDateToString((Date)properties[i].getPropertyValue(), ResLoader.getMessages("patternTimestamp")));
                    }
                    sql = (new StringBuilder()).append(sql).append(properties[i].getPropertyName()).append("=").append(pfx).append(properties[i].getPropertyValue()).append(sfx).append(",").toString();
                }
                pfx = "";
                sfx = "";
            }

            if(!properties[properties.length - 1].getPropertyName().equalsIgnoreCase("class") && properties[properties.length - 1].getPropertyValue() != null)
            {

                
                if(properties[properties.length - 1].getPropertyValue().getClass().equals(S.getClass()))
                {
                    pfx = sfx = "'";
                    properties[properties.length - 1].setPropertyValue(properties[properties.length - 1].getPropertyValue().toString().replaceAll("'", "''"));
                }
                if(properties[properties.length - 1].getPropertyValue().getClass().equals(D.getClass()))
                {
                    pfx = ResLoader.getMessages("pfxDate");
                    sfx = ResLoader.getMessages("sfxDate");
                    properties[properties.length - 1].setPropertyValue(Utility.convertDateToString((java.sql.Date)properties[properties.length - 1].getPropertyValue(), ResLoader.getMessages("patternDate")));
                }
                if(properties[properties.length - 1].getPropertyValue().getClass().equals(T.getClass()))
                {
                    pfx = ResLoader.getMessages("pfxTimestamp");
                    sfx = ResLoader.getMessages("sfxTimestamp");
                    properties[properties.length - 1].setPropertyValue(Utility.convertDateToString((Date)properties[properties.length - 1].getPropertyValue(), ResLoader.getMessages("patternTimestamp")));
                }
                sql = (new StringBuilder()).append(sql).append(properties[properties.length - 1].getPropertyName()).append("=").append(pfx).append(properties[properties.length - 1].getPropertyValue()).append(sfx).append(" WHERE ").append(where).append("").toString();
            } else
            {
                sql = (new StringBuilder()).append(sql.substring(0, sql.length() - 1)).append(" WHERE ").append(where).append("").toString();
            }
            if(!sql.equals((new StringBuilder()).append("UPDATE ").append(table).append(" SET ").toString()))
            {
                executeUpdate(sql);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    
    public boolean updateRowByObject(String sql, Object object)
    {
        Property properties[] = BeanInfoFactory.getProperties(object);
        try
        {
            ResultSet rs = execute(sql);
            for(int i = 0; i < properties.length; i++)
            {
                if(properties[i].getPropertyName().compareToIgnoreCase("class") != 0)
                {
                    rs.updateObject(properties[i].getPropertyName(), properties[i].getPropertyValue());
                }
            }

            rs.updateRow();
            closeQuery(rs);
        }
        catch(Exception e)
        {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    public Object[] retrieveRowAsObject(String sql, Object object)
    {
        Object result[] = null;
        try
        {
            ResultSet rs = execute(sql);
            if(rs == null)
            {
                return null;
            }
            rs.last();
            Class objectClass = object.getClass();
            int nbRows = rs.getRow();
            result = (Object[])(Object[])Array.newInstance(objectClass, nbRows);
            Property properties[] = BeanInfoFactory.getProperties(object);
            rs.beforeFirst();
            while(rs.next()) 
            {
                result[rs.getRow() - 1] = objectClass.newInstance();
                int i = 0;
                while(i < properties.length) 
                {
                    if(!properties[i].getPropertyName().equalsIgnoreCase("class"))
                    {
                        System.out.println("Nom propriete="+properties[i].getPropertyName());
                        properties[i].setPropertyValue(rs.getObject(properties[i].getPropertyName()));
                        BeanInfoFactory.setObjectPropertyValue(result[rs.getRow() - 1], properties[i]);
                    }
                    i++;
                }
            }
            closeQuery(rs);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            setMessage(e.getMessage());
            System.out.println(e.getMessage());
            return result;
        }
        return result;
    }

    public Object[] retrieveRowAsObject(PreparedStatement preparedStatement, Object object)
    {
        Object result[] = null;

        try
        {
            ResultSet rs = pstmt.executeQuery();
            if(rs == null)
            {
                return null;
            }           
            rs.last();
            Class objectClass = object.getClass();
            int nbRows = rs.getRow();
            result = (Object[])(Object[])Array.newInstance(objectClass, nbRows);
            Property properties[] = BeanInfoFactory.getProperties(object);
            rs.beforeFirst();
            while(rs.next()) 
            {
                result[rs.getRow() - 1] = objectClass.newInstance();
                int i = 0;
                while(i < properties.length) 
                {
                    //System.out.println(properties[i].getPropertyName());
                    if(!properties[i].getPropertyName().equalsIgnoreCase("class"))
                    {
                        properties[i].setPropertyValue(rs.getObject(properties[i].getPropertyName()));
                        BeanInfoFactory.setObjectPropertyValue(result[rs.getRow() - 1], properties[i]);
                    }
                    i++;
                }
            }
            closeQuery(rs);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            setMessage(e.getMessage());
            System.out.println(e.getMessage());
            return result;
        }
        return result;
    }
  
    public double getResultOfSQLFunction(String requete)
    {
        double result = 0.0D;
        try
        {
            ResultSet rs = execute(requete);
            rs.last();
            result = rs.getInt(1);
            closeQuery(rs);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            setMessage(e.getMessage());
        }
        return result;
    }

    public int getNumberOfRows(String table)
    {
        String sql = (new StringBuilder()).append("select count(*) from ").append(table).toString();
        int nb = 0;
        try
        {
            ResultSet rs = execute(sql);
            nb = rs.getInt(1);
            closeQuery(rs);
            System.out.println(sql);
        }
        catch(Exception e)
        {
            setMessage(e.getMessage());
        }
        return nb;
    }

    public int getNumberOfRows2(String table)
    {
        String sql = (new StringBuilder()).append("select * from ").append(table).toString();
        int nbRows = 0;
        try
        {
            ResultSet rs = execute(sql);
            rs.last();
            nbRows = rs.getRow();
            closeQuery(rs);
            System.out.println(sql);
        }
        catch(Exception e)
        {
            setMessage(e.getMessage());
        }
        return nbRows;
    }
    public int getNumRowsBySql(String sql)
    {
        int nbRows = 0;
        try
        {
            ResultSet r = execute(sql);
            r.last();
            nbRows = r.getRow();
            closeQuery(r);
            System.out.println(sql);
        }
        catch(Exception e)
        {
            setMessage(e.getMessage());
        }
        return nbRows;
    }

    public Connection getConn()
    {
        return conn;
    }

    public void setConn(Connection conn)
    {
        this.conn = conn;
    }

    public ResultSet getRsltSet()
    {
        return rsltSet;
    }

    public void setRsltSet(ResultSet rsltSet)
    {
        this.rsltSet = rsltSet;
    }

    public ResultSetMetaData getRsltMetaData()
    {
        return rsltMetaData;
    }

    public void setRsltMetaData(ResultSetMetaData rsltMetaData)
    {
        this.rsltMetaData = rsltMetaData;
    }

    public DatabaseMetaData getDbMetaData()
    {
        return dbMetaData;
    }

    public void setDbMetaData(DatabaseMetaData dbMetaData)
    {
        this.dbMetaData = dbMetaData;
    }

    public String getCatalog()
    {
        return catalog;
    }

    public void setCatalog(String catalog)
    {
        this.catalog = catalog;
    }

    public String[] getTypes()
    {
        return types;
    }

    public void setTypes(String types[])
    {
        this.types = types;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
        System.out.println(message);
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public Properties getConnProperties()
    {
        return connProperties;
    }

    public void setConnProperties(Properties properties)
    {
        connProperties = properties;
    }

    public void closeQuery(ResultSet rs)
        throws SQLException
    {
        if(rs != null)
        {
            rs.close();
        }
        if(pstmt != null)
        {
            pstmt.close();
        }
    }
}
