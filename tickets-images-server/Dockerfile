FROM node:14-alpine as base
WORKDIR /src
RUN yarn add sharp
RUN yarn install --unsafe-perm
COPY package*.json /src
EXPOSE 3000
COPY . /src
CMD ["node", "app.js"]