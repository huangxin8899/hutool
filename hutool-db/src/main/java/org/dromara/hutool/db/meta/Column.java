/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.meta;

import org.dromara.hutool.core.util.BooleanUtil;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.db.DbException;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库表的列信息
 *
 * @author loolly
 */
public class Column implements Serializable, Cloneable {
	private static final long serialVersionUID = 577527740359719367L;

	// ----------------------------------------------------- Fields start
	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 列名
	 */
	private String name;
	/**
	 * 类型，对应java.sql.Types中的类型
	 */
	private int type;
	/**
	 * 类型名称
	 */
	private String typeName;
	/**
	 * 大小或数据长度
	 */
	private long size;
	/**
	 * 保留小数位数
	 */
	private Integer digit;
	/**
	 * 是否为可空
	 */
	private boolean isNullable;
	/**
	 * 注释
	 */
	private String remarks;
	/**
	 * 是否自增
	 */
	private boolean autoIncrement;
	/**
	 * 字段默认值<br>
	 * default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be {@code null})
	 */
	private String columnDef;
	/**
	 * 是否为主键
	 */
	private boolean isPk;
	/**
	 * 列字段顺序
	 */
	private int order;
// ----------------------------------------------------- Fields end

	/**
	 * 创建列对象
	 *
	 * @param columnMetaRs 列元信息的ResultSet
	 * @param table        表信息
	 * @return 列对象
	 * @since 5.4.3
	 */
	public static Column of(final Table table, final ResultSet columnMetaRs) {
		return new Column(table, columnMetaRs);
	}

	// ----------------------------------------------------- Constructor start

	/**
	 * 构造
	 */
	public Column() {
	}

	/**
	 * 构造
	 *
	 * @param table        表信息
	 * @param columnMetaRs Meta信息的ResultSet
	 * @since 5.4.3
	 */
	public Column(final Table table, final ResultSet columnMetaRs) {
		try {
			init(table, columnMetaRs);
		} catch (final SQLException e) {
			throw new DbException(e, "Get table [{}] meta info error!", tableName);
		}
	}
	// ----------------------------------------------------- Constructor end

	/**
	 * 初始化
	 *
	 * @param table        表信息
	 * @param columnMetaRs 列的meta ResultSet
	 * @throws SQLException SQL执行异常
	 */
	public void init(final Table table, final ResultSet columnMetaRs) throws SQLException {
		this.tableName = table.getTableName();

		this.name = columnMetaRs.getString("COLUMN_NAME");
		this.isPk = table.isPk(this.name);

		this.type = columnMetaRs.getInt("DATA_TYPE");

		String typeName = columnMetaRs.getString("TYPE_NAME");
		//issue#2201@Gitee
		typeName = ReUtil.delLast("\\(\\d+\\)", typeName);
		this.typeName = typeName;

		this.size = columnMetaRs.getLong("COLUMN_SIZE");
		this.isNullable = columnMetaRs.getBoolean("NULLABLE");
		this.remarks = columnMetaRs.getString("REMARKS");
		this.columnDef = columnMetaRs.getString("COLUMN_DEF");
		this.order = columnMetaRs.getRow();
		// 保留小数位数
		try {
			this.digit = columnMetaRs.getInt("DECIMAL_DIGITS");
		} catch (final SQLException ignore) {
			//某些驱动可能不支持，跳过
		}

		// 是否自增
		try {
			final String auto = columnMetaRs.getString("IS_AUTOINCREMENT");
			if (BooleanUtil.toBoolean(auto)) {
				this.autoIncrement = true;
			}
		} catch (final SQLException ignore) {
			//某些驱动可能不支持，跳过
		}
	}

	// ----------------------------------------------------- Getters and Setters start

	/**
	 * 获取表名
	 *
	 * @return 表名
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 设置表名
	 *
	 * @param tableName 表名
	 * @return this
	 */
	public Column setTableName(final String tableName) {
		this.tableName = tableName;
		return this;
	}

	/**
	 * 获取列名
	 *
	 * @return 列名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置列名
	 *
	 * @param name 列名
	 * @return this
	 */
	public Column setName(final String name) {
		this.name = name;
		return this;
	}

	/**
	 * 获取字段类型的枚举
	 *
	 * @return 阻断类型枚举
	 * @since 4.5.8
	 */
	public JdbcType getTypeEnum() {
		return JdbcType.valueOf(this.type);
	}

	/**
	 * 获取类型，对应{@link java.sql.Types}中的类型
	 *
	 * @return 类型
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置类型，对应java.sql.Types中的类型
	 *
	 * @param type 类型
	 * @return this
	 */
	public Column setType(final int type) {
		this.type = type;
		return this;
	}

	/**
	 * 获取类型名称
	 *
	 * @return 类型名称
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * 设置类型名称
	 *
	 * @param typeName 类型名称
	 * @return this
	 */
	public Column setTypeName(final String typeName) {
		this.typeName = typeName;
		return this;
	}

	/**
	 * 获取大小或数据长度
	 *
	 * @return 大小或数据长度
	 */
	public long getSize() {
		return size;
	}

	/**
	 * 设置大小或数据长度
	 *
	 * @param size 大小或数据长度
	 * @return this
	 */
	public Column setSize(final long size) {
		this.size = size;
		return this;
	}

	/**
	 * 获取小数位数
	 *
	 * @return 大小或数据长度
	 */
	public Integer getDigit() {
		return digit;
	}

	/**
	 * 设置小数位数
	 *
	 * @param digit 小数位数
	 * @return this
	 */
	public Column setDigit(final int digit) {
		this.digit = digit;
		return this;
	}

	/**
	 * 是否为可空
	 *
	 * @return 是否为可空
	 */
	public boolean isNullable() {
		return isNullable;
	}

	/**
	 * 设置是否为可空
	 *
	 * @param isNullable 是否为可空
	 * @return this
	 */
	public Column setNullable(final boolean isNullable) {
		this.isNullable = isNullable;
		return this;
	}

	/**
	 * 获取注释
	 *
	 * @return 注释
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * 设置注释
	 *
	 * @param remarks 注释
	 * @return this
	 */
	public Column setRemarks(final String remarks) {
		this.remarks = remarks;
		return this;
	}

	/**
	 * 是否自增
	 *
	 * @return 是否自增
	 * @since 5.4.3
	 */
	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	/**
	 * 设置是否自增
	 *
	 * @param autoIncrement 是否自增
	 * @return this
	 * @since 5.4.3
	 */
	public Column setAutoIncrement(final boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
		return this;
	}

	/**
	 * 获取默认值
	 *
	 * @return 默认值
	 */
	public String getColumnDef() {
		return columnDef;
	}

	/**
	 * 设置默认值
	 *
	 * @param columnDef 默认值
	 * @return this
	 */
	public Column setColumnDef(final String columnDef) {
		this.columnDef = columnDef;
		return this;
	}

	/**
	 * 是否主键
	 *
	 * @return 是否主键
	 * @since 5.4.3
	 */
	public boolean isPk() {
		return isPk;
	}

	/**
	 * 设置是否主键
	 *
	 * @param isPk 是否主键
	 * @return this
	 * @since 5.4.3
	 */
	public Column setPk(final boolean isPk) {
		this.isPk = isPk;
		return this;
	}

	/**
	 * 获取顺序号
	 *
	 * @return 顺序号
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * 设置顺序号
	 *
	 * @param order 顺序号
	 * @return this
	 */
	public Column setOrder(final int order) {
		this.order = order;
		return this;
	}

	// ----------------------------------------------------- Getters and Setters end

	@Override
	public String toString() {
		return "Column [tableName=" + tableName + ", name=" + name + ", type=" + type + ", size=" + size + ", isNullable=" + isNullable + ", order=" + order + "]";
	}

	@Override
	public Column clone() throws CloneNotSupportedException {
		return (Column) super.clone();
	}
}
