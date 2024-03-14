FROM node:21

WORKDIR /app

COPY package.json .

RUN npm install

COPY . .

EXPOSE 8079

CMD ["npm", "start"]