package com.common.utils;

public class Query {
    private Integer current;
    private Integer size;
    private String ascs;
    private String descs;

    public Query() {
    }

    public Integer getCurrent() {
        return this.current;
    }

    public Integer getSize() {
        return this.size;
    }

    public String getAscs() {
        return this.ascs;
    }

    public String getDescs() {
        return this.descs;
    }

    public Query setCurrent(final Integer current) {
        this.current = current;
        return this;
    }

    public Query setSize(final Integer size) {
        this.size = size;
        return this;
    }

    public Query setAscs(final String ascs) {
        this.ascs = ascs;
        return this;
    }

    public Query setDescs(final String descs) {
        this.descs = descs;
        return this;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Query)) {
            return false;
        } else {
            Query other = (Query)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label59: {
                    Object this$current = this.getCurrent();
                    Object other$current = other.getCurrent();
                    if (this$current == null) {
                        if (other$current == null) {
                            break label59;
                        }
                    } else if (this$current.equals(other$current)) {
                        break label59;
                    }

                    return false;
                }

                Object this$size = this.getSize();
                Object other$size = other.getSize();
                if (this$size == null) {
                    if (other$size != null) {
                        return false;
                    }
                } else if (!this$size.equals(other$size)) {
                    return false;
                }

                Object this$ascs = this.getAscs();
                Object other$ascs = other.getAscs();
                if (this$ascs == null) {
                    if (other$ascs != null) {
                        return false;
                    }
                } else if (!this$ascs.equals(other$ascs)) {
                    return false;
                }

                Object this$descs = this.getDescs();
                Object other$descs = other.getDescs();
                if (this$descs == null) {
                    if (other$descs != null) {
                        return false;
                    }
                } else if (!this$descs.equals(other$descs)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Query;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return "Query(current=" + this.getCurrent() + ", size=" + this.getSize() + ", ascs=" + this.getAscs() + ", descs=" + this.getDescs() + ")";
    }
}
