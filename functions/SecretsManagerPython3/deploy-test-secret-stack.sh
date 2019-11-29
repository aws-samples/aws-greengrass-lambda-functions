#!/usr/bin/env bash

aws cloudformation deploy --template-file test-secret.cf.yaml --stack-name test-secret-stack
