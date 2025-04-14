import express from 'express'
import dotenv from 'dotenv'
import cors from 'cors'

const app = express()
dotenv.config()
app.use(cors())
app.use(express.json())

const port = 3000

app.get("/", (req, res) => {
  res.send("I'm alive!");
});
  
app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}`);
});