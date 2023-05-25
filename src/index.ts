import express, { Express } from 'express';
import dotenv from 'dotenv';

import setRoutes from './routes/main';

dotenv.config();

const app: Express = express();
const port = process.env.PORT;

setRoutes(app);

app.listen(port, () => {
  console.log(`⚡️[server]: Server is running at http://localhost:${port}`);
});
