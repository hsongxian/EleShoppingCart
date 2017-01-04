package com.song.eleshoppingcart.bean;


/**
 * Created by Administrator on 2016/10/5 0005.
 */
public class Commodity {


        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        private String name;

        private int position;

        private int icon;

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        private String content;

        private String sales;

        private String sum;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private String type;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getSales() {
            return sales;
        }

        public void setSales(String sales) {
            this.sales = sales;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

}
