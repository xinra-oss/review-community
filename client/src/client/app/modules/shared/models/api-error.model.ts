export interface ApiError {
  timestamp: number;
  status: number;
  error: string;
  message: string;
  path: string;
}