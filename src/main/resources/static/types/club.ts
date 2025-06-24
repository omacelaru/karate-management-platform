import { UserinfoResponse } from "openid-client";

export interface ClubRequest {
  name: string;
  acronym: string;
  city: string;
  address: string;
  phone: string;
  email: string;
}

export interface ClubResponse {
  id: number;
  name: string;
  acronym: string;
  city: string;
  address: string;
  phone: string;
  email: string;
  createdAt: string;
  updatedAt: string;
}

export interface CoachResponse {
  user: UserinfoResponse;
  id: number;
  firstName: string;
  lastName: string;
  email: string;
}

export interface ClubWithCoachesResponse extends ClubResponse {
  coaches: CoachResponse[];
} 