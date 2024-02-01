package com.ruomm.assistx.greendao;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        /**
         * new Schema(version, defaultJavaPackage);<br/>
         * 指定版本和生成的类的包路径
         */

        Schema schema = new Schema(1, "com.ruomm.appcorex.greendao.dbstore");
        /**
         * 添加模型
         */
//		addNote(schema);
//		addMessge(schema);
        addPropertySave(schema);
        /**
         * 生成GreenDao模型new DaoGenerator().generateAll(schema, outDir);<br/>
         * 模型的来源和解析模型后Java类存放路径
         */
        new DaoGenerator().generateAll(schema, "../assistx_greendao/src-gen");
    }

    /**
     * ORM模型建表示例
     *
     * @param schema
     */
    private static void addNote(Schema schema) {
        /**
         * 添加数据库表<br/>
         * <ul>
         * <li>addEntity(String className) 设置数据库关联类名称</li>
         * </ul>
         */
        Entity note = schema.addEntity("Note");
        /**
         * 添加其他属性<br/>
         * <ul>
         * <li>primaryKey() 设置为主键</li>
         * <li>autoincrement() 自增长，对于整型非空有效</li>
         * <li>notNull() 不可为空</li>
         * <li>columnName(String columnName)设置列名，不调用则列名和Field的名称相同</li>
         * <li>设置主键可以使用下面方法中的一个</li>
         * <li>note.addIdProperty(),主键名称自动为"_id"</li>
         * <li>note.addLongProperty("id").primaryKey().autoincrement().notNull(), 可自定义主键名称和列名</li>
         * </ul>
         */
        note.addIdProperty();
        note.addStringProperty("mytext").columnName("text").notNull();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
        /**
         * 设置数据库表名<br/>
         * <ul>
         * <li>note.setTableName(String tableName) 设置表名，不调用则和关联类名相同</li>
         * </ul>
         */
        note.setTableName("note");
    }

    // 建立一张Message表
    private static void addMessge(Schema schema) {
        Entity note = schema.addEntity("Message");
        note.addIdProperty().columnName("msgID");
        note.addStringProperty("text").columnName("msg_text").notNull();
        note.addStringProperty("comment").columnName("msg_comment");
        note.addDateProperty("date").columnName("msg_date");
    }

    // 建立一张Message表
    private static void addPropertySave(Schema schema) {
        Entity note = schema.addEntity("DBEntryValue");
        note.addIdProperty().columnName("id").autoincrement().notNull();
        note.addStringProperty("key").columnName("key").notNull();
        note.addStringProperty("tag").columnName("tag");
        note.addStringProperty("valueTag").columnName("value_tag");
        note.addStringProperty("value").columnName("value");
        note.addDateProperty("updateTime").columnName("update_time");
    }

}
