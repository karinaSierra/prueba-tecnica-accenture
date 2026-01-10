#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { FranquiciasApiStack } from '../lib/franquicias-stack';

const app = new cdk.App();

// Configuración por defecto para entorno de prueba
const env = {
  account: process.env.CDK_DEFAULT_ACCOUNT,
  region: process.env.CDK_DEFAULT_REGION || 'us-east-1',
};

// Props compartidas
const stackProps: cdk.StackProps = {
  env,
  description: 'Franquicias API - Reactive REST API Infrastructure',
};

// Stack principal
new FranquiciasApiStack(app, 'FranquiciasApiStack', {
  ...stackProps,
  stackName: 'franquicias-api-stack',
  tags: {
    Project: 'FranquiciasAPI',
    Environment: 'dev',
    ManagedBy: 'CDK',
  },
});


