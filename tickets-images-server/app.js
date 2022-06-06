const express = require("express");
const dotenv = require("dotenv");
const cors = require("cors");

const corsOptions = {
  origin: "https://enigmatic-springs-73519.herokuapp.com",
  optionsSuccessStatus: 200,
};
dotenv.config();
const bodyParser = require("body-parser");
const router = require("./router");
const app = express();

app.use(bodyParser.json());
app.use(cors(corsOptions));
app.use(bodyParser.urlencoded({ extended: true }));

app.use(express.static(__dirname + "/data/img"));
app.use("/upload", router);

app.listen(process.env.PORT || 3500, function () {
  console.log(`Express server is listening on port ${process.env.PORT}`);
});
