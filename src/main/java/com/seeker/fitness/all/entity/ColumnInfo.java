package com.seeker.fitness.all.entity;

import com.alibaba.fastjson.annotation.JSONField;

public class ColumnInfo {
    @JSONField(name = "TABLE_SCHEMA")
    private String TABLE_SCHEMA;//表所有者（对于schema的名称）

    @JSONField(name = "TABLE_NAME")
    private String TABLE_NAME;//表名

    @JSONField(name = "COLUMN_NAME")
    private String COLUMN_NAME;//列名

    @JSONField(name = "ORDINAL_POSITION")
    private String ORDINAL_POSITION;//列标识号

    @JSONField(name = "COLUMN_DEFAULT")
    private String COLUMN_DEFAULT;//列的默认值

    @JSONField(name = "IS_NULLABLE")
    private String IS_NULLABLE;//列的为空性。如果列允许 null，那么该列返回 yes。否则，返回 no

    @JSONField(name = "DATA_TYPE")
    private String DATA_TYPE;//系统提供的数据类型

    @JSONField(name = "CHARACTER_MAXIMUM_LENGTH")
    private String CHARACTER_MAXIMUM_LENGTH;//以字符为单位的最大长度，适于二进制数据、字符数据，或者文本和图像数据。否则，返回 null。有关更多信息，请参见数据类型

    @JSONField(name = "CHARACTER_OCTET_LENGTH")
    private String CHARACTER_OCTET_LENGTH;//以字节为单位的最大长度，适于二进制数据、字符数据，或者文本和图像数据。否则，返回 nu

    @JSONField(name = "NUMERIC_PRECISION")
    private String NUMERIC_PRECISION;//近似数字数据、精确数字数据、整型数据或货币数据的精度。否则，返回 null

    @JSONField(name = "NUMERIC_PRECISION_RADIX")
    private String NUMERIC_PRECISION_RADIX;//近似数字数据、精确数字数据、整型数据或货币数据的精度基数。否则，返回 null

    @JSONField(name = "NUMERIC_SCALE")
    private String NUMERIC_SCALE;//近似数字数据、精确数字数据、整数数据或货币数据的小数位数。否则，返回 null

    @JSONField(name = "DATETIME_PRECISION")
    private String DATETIME_PRECISION;//datetime 及 sql-92 interval 数据类型的子类型代码。对于其它数据类型，返回 null

    @JSONField(name = "CHARACTER_SET_CATALOG")
    private String CHARACTER_SET_CATALOG;//如果列是字符数据或 text 数据类型，那么返回 master，指明字符集所在的数据库。否则，返回 null

    @JSONField(name = "CHARACTER_SET_SCHEMA")
    private String CHARACTER_SET_SCHEMA;//如果列是字符数据或 text 数据类型，那么返回 dbo，指明字符集的所有者名称。否则，返回 null

    @JSONField(name = "CHARACTER_SET_NAME")
    private String CHARACTER_SET_NAME;//如果该列是字符数据或 text 数据类型，那么为字符集返回唯一的名称。否则，返回 null

    @JSONField(name = "COLLATION_CATALOG")
    private String COLLATION_CATALOG;//如果列是字符数据或 text 数据类型，那么返回 master，指明在其中定义排序次序的数据库。否则此列为 null

    @JSONField(name = "COLLATION_SCHEMA")
    private String COLLATION_SCHEMA;//返回 dbo，为字符数据或 text 数据类型指明排序次序的所有者。否则，返回 null

    @JSONField(name = "COLLATION_NAME")
    private String COLLATION_NAME;//如果列是字符数据或 text 数据类型，那么为排序次序返回唯一的名称。否则，返回 null。

    @JSONField(name = "DOMAIN_CATALOG")
    private String DOMAIN_CATALOG;//如果列是一种用户定义数据类型，那么该列是某个数据库名称，在该数据库名中创建了这种用户定义数据类型。否则，返回 null

    @JSONField(name = "DOMAIN_SCHEMA")
    private String DOMAIN_SCHEMA;//如果列是一种用户定义数据类型，那么该列是这种用户定义数据类型的创建者。否则，返回 null

    @JSONField(name = "DOMAIN_NAME")
    private String DOMAIN_NAME;//如果列是一种用户定义数据类型，那么该列是这种用户定义数据类型的名称。否则，返回 NULL

    @JSONField(name = "COLUMN_COMMENT")
    private String COLUMN_COMMENT;//列的描述信息

    @JSONField(name = "COLUMN_TYPE")
    private String COLUMN_TYPE;//字段类型

    @JSONField(name = "COLUMN_KEY")
    private String COLUMN_KEY;//特性(是否唯一，是否主键等)

    @JSONField(name = "EXTRA")
    private String EXTRA;//是否自增属于此字段 其他未可知

    @JSONField(name = "PRIVILEGES")
    private String PRIVILEGES;

    @JSONField(name = "GENERATION_EXPRESSION")
    private String GENERATION_EXPRESSION;

    @JSONField(name = "SRS_ID")
    private String SRS_ID;

    @JSONField(name = "TABLE_CATALOG")
    private String TABLE_CATALOG;

    public String getTABLE_SCHEMA() {
        return TABLE_SCHEMA;
    }

    public void setTABLE_SCHEMA(String TABLE_SCHEMA) {
        this.TABLE_SCHEMA = TABLE_SCHEMA;
    }

    public String getTABLE_NAME() {
        return TABLE_NAME;
    }

    public void setTABLE_NAME(String TABLE_NAME) {
        this.TABLE_NAME = TABLE_NAME;
    }

    public String getCOLUMN_NAME() {
        return COLUMN_NAME;
    }

    public void setCOLUMN_NAME(String COLUMN_NAME) {
        this.COLUMN_NAME = COLUMN_NAME;
    }

    public String getORDINAL_POSITION() {
        return ORDINAL_POSITION;
    }

    public void setORDINAL_POSITION(String ORDINAL_POSITION) {
        this.ORDINAL_POSITION = ORDINAL_POSITION;
    }

    public String getCOLUMN_DEFAULT() {
        return COLUMN_DEFAULT;
    }

    public void setCOLUMN_DEFAULT(String COLUMN_DEFAULT) {
        this.COLUMN_DEFAULT = COLUMN_DEFAULT;
    }

    public String getIS_NULLABLE() {
        return IS_NULLABLE;
    }

    public void setIS_NULLABLE(String IS_NULLABLE) {
        this.IS_NULLABLE = IS_NULLABLE;
    }

    public String getDATA_TYPE() {
        return DATA_TYPE;
    }

    public void setDATA_TYPE(String DATA_TYPE) {
        this.DATA_TYPE = DATA_TYPE;
    }

    public String getCHARACTER_MAXIMUM_LENGTH() {
        return CHARACTER_MAXIMUM_LENGTH;
    }

    public void setCHARACTER_MAXIMUM_LENGTH(String CHARACTER_MAXIMUM_LENGTH) {
        this.CHARACTER_MAXIMUM_LENGTH = CHARACTER_MAXIMUM_LENGTH;
    }

    public String getCHARACTER_OCTET_LENGTH() {
        return CHARACTER_OCTET_LENGTH;
    }

    public void setCHARACTER_OCTET_LENGTH(String CHARACTER_OCTET_LENGTH) {
        this.CHARACTER_OCTET_LENGTH = CHARACTER_OCTET_LENGTH;
    }

    public String getNUMERIC_PRECISION() {
        return NUMERIC_PRECISION;
    }

    public void setNUMERIC_PRECISION(String NUMERIC_PRECISION) {
        this.NUMERIC_PRECISION = NUMERIC_PRECISION;
    }

    public String getNUMERIC_PRECISION_RADIX() {
        return NUMERIC_PRECISION_RADIX;
    }

    public void setNUMERIC_PRECISION_RADIX(String NUMERIC_PRECISION_RADIX) {
        this.NUMERIC_PRECISION_RADIX = NUMERIC_PRECISION_RADIX;
    }

    public String getNUMERIC_SCALE() {
        return NUMERIC_SCALE;
    }

    public void setNUMERIC_SCALE(String NUMERIC_SCALE) {
        this.NUMERIC_SCALE = NUMERIC_SCALE;
    }

    public String getDATETIME_PRECISION() {
        return DATETIME_PRECISION;
    }

    public void setDATETIME_PRECISION(String DATETIME_PRECISION) {
        this.DATETIME_PRECISION = DATETIME_PRECISION;
    }

    public String getCHARACTER_SET_CATALOG() {
        return CHARACTER_SET_CATALOG;
    }

    public void setCHARACTER_SET_CATALOG(String CHARACTER_SET_CATALOG) {
        this.CHARACTER_SET_CATALOG = CHARACTER_SET_CATALOG;
    }

    public String getCHARACTER_SET_SCHEMA() {
        return CHARACTER_SET_SCHEMA;
    }

    public void setCHARACTER_SET_SCHEMA(String CHARACTER_SET_SCHEMA) {
        this.CHARACTER_SET_SCHEMA = CHARACTER_SET_SCHEMA;
    }

    public String getCHARACTER_SET_NAME() {
        return CHARACTER_SET_NAME;
    }

    public void setCHARACTER_SET_NAME(String CHARACTER_SET_NAME) {
        this.CHARACTER_SET_NAME = CHARACTER_SET_NAME;
    }

    public String getCOLLATION_CATALOG() {
        return COLLATION_CATALOG;
    }

    public void setCOLLATION_CATALOG(String COLLATION_CATALOG) {
        this.COLLATION_CATALOG = COLLATION_CATALOG;
    }

    public String getCOLLATION_SCHEMA() {
        return COLLATION_SCHEMA;
    }

    public void setCOLLATION_SCHEMA(String COLLATION_SCHEMA) {
        this.COLLATION_SCHEMA = COLLATION_SCHEMA;
    }

    public String getCOLLATION_NAME() {
        return COLLATION_NAME;
    }

    public void setCOLLATION_NAME(String COLLATION_NAME) {
        this.COLLATION_NAME = COLLATION_NAME;
    }

    public String getDOMAIN_CATALOG() {
        return DOMAIN_CATALOG;
    }

    public void setDOMAIN_CATALOG(String DOMAIN_CATALOG) {
        this.DOMAIN_CATALOG = DOMAIN_CATALOG;
    }

    public String getDOMAIN_SCHEMA() {
        return DOMAIN_SCHEMA;
    }

    public void setDOMAIN_SCHEMA(String DOMAIN_SCHEMA) {
        this.DOMAIN_SCHEMA = DOMAIN_SCHEMA;
    }

    public String getDOMAIN_NAME() {
        return DOMAIN_NAME;
    }

    public void setDOMAIN_NAME(String DOMAIN_NAME) {
        this.DOMAIN_NAME = DOMAIN_NAME;
    }

    public String getCOLUMN_COMMENT() {
        return COLUMN_COMMENT;
    }

    public void setCOLUMN_COMMENT(String COLUMN_COMMENT) {
        this.COLUMN_COMMENT = COLUMN_COMMENT;
    }

    public String getCOLUMN_TYPE() {
        return COLUMN_TYPE;
    }

    public void setCOLUMN_TYPE(String COLUMN_TYPE) {
        this.COLUMN_TYPE = COLUMN_TYPE;
    }

    public String getCOLUMN_KEY() {
        return COLUMN_KEY;
    }

    public void setCOLUMN_KEY(String COLUMN_KEY) {
        this.COLUMN_KEY = COLUMN_KEY;
    }

    public String getEXTRA() {
        return EXTRA;
    }

    public void setEXTRA(String EXTRA) {
        this.EXTRA = EXTRA;
    }

    public String getPRIVILEGES() {
        return PRIVILEGES;
    }

    public void setPRIVILEGES(String PRIVILEGES) {
        this.PRIVILEGES = PRIVILEGES;
    }

    public String getGENERATION_EXPRESSION() {
        return GENERATION_EXPRESSION;
    }

    public void setGENERATION_EXPRESSION(String GENERATION_EXPRESSION) {
        this.GENERATION_EXPRESSION = GENERATION_EXPRESSION;
    }

    public String getSRS_ID() {
        return SRS_ID;
    }

    public void setSRS_ID(String SRS_ID) {
        this.SRS_ID = SRS_ID;
    }

    public String getTABLE_CATALOG() {
        return TABLE_CATALOG;
    }

    public void setTABLE_CATALOG(String TABLE_CATALOG) {
        this.TABLE_CATALOG = TABLE_CATALOG;
    }

    @Override
    public String toString() {
        return "ColumnInfo{" +
                "TABLE_SCHEMA='" + TABLE_SCHEMA + '\'' +
                ", TABLE_NAME='" + TABLE_NAME + '\'' +
                ", COLUMN_NAME='" + COLUMN_NAME + '\'' +
                ", ORDINAL_POSITION='" + ORDINAL_POSITION + '\'' +
                ", COLUMN_DEFAULT='" + COLUMN_DEFAULT + '\'' +
                ", IS_NULLABLE='" + IS_NULLABLE + '\'' +
                ", DATA_TYPE='" + DATA_TYPE + '\'' +
                ", CHARACTER_MAXIMUM_LENGTH='" + CHARACTER_MAXIMUM_LENGTH + '\'' +
                ", CHARACTER_OCTET_LENGTH='" + CHARACTER_OCTET_LENGTH + '\'' +
                ", NUMERIC_PRECISION='" + NUMERIC_PRECISION + '\'' +
                ", NUMERIC_PRECISION_RADIX='" + NUMERIC_PRECISION_RADIX + '\'' +
                ", NUMERIC_SCALE='" + NUMERIC_SCALE + '\'' +
                ", DATETIME_PRECISION='" + DATETIME_PRECISION + '\'' +
                ", CHARACTER_SET_CATALOG='" + CHARACTER_SET_CATALOG + '\'' +
                ", CHARACTER_SET_SCHEMA='" + CHARACTER_SET_SCHEMA + '\'' +
                ", CHARACTER_SET_NAME='" + CHARACTER_SET_NAME + '\'' +
                ", COLLATION_CATALOG='" + COLLATION_CATALOG + '\'' +
                ", COLLATION_SCHEMA='" + COLLATION_SCHEMA + '\'' +
                ", COLLATION_NAME='" + COLLATION_NAME + '\'' +
                ", DOMAIN_CATALOG='" + DOMAIN_CATALOG + '\'' +
                ", DOMAIN_SCHEMA='" + DOMAIN_SCHEMA + '\'' +
                ", DOMAIN_NAME='" + DOMAIN_NAME + '\'' +
                ", COLUMN_COMMENT='" + COLUMN_COMMENT + '\'' +
                ", COLUMN_TYPE='" + COLUMN_TYPE + '\'' +
                ", COLUMN_KEY='" + COLUMN_KEY + '\'' +
                ", EXTRA='" + EXTRA + '\'' +
                ", PRIVILEGES='" + PRIVILEGES + '\'' +
                ", GENERATION_EXPRESSION='" + GENERATION_EXPRESSION + '\'' +
                ", SRS_ID='" + SRS_ID + '\'' +
                ", TABLE_CATALOG='" + TABLE_CATALOG + '\'' +
                '}';
    }
}
