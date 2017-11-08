// app
import { HomeRoutes } from './home/home.routes';
import { AboutRoutes } from './about/about.routes';
import { CategoryRoutes } from '../modules/category';
import { ProductRoutes } from '../modules/product'
import { AuthRoutes } from '../modules/auth';

export const routes: Array<any> = [
  ...HomeRoutes,
  ...AboutRoutes,
  ...CategoryRoutes,
  ...ProductRoutes,
  ...AuthRoutes
];
