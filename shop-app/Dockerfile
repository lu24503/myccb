FROM registry.cn-shenzhen.aliyuncs.com/sumply-shop/openjdk8-font
ADD target/cereshop-app /home/cereshop-app
ADD cert/ /home/ceres/cert
COPY font/puhui/ /usr/share/fonts/ttf-dejavu
WORKDIR /home/cereshop-app
ENTRYPOINT ["java","-jar","cereshop-app.jar"]
