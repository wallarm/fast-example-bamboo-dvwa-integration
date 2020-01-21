# README

This is an example of Wallarm FAST running security tests in the Bamboo pipeline. The target application is a DVWA.

## How to reproduce example

1. Create your FAST node and get `WALLARM_API_TOKEN` here https://us1.my.wallarm.com/nodes
2. Fork this repository
3. Add project into Bamboo (first build will fail without `WALLARM_API_TOKEN`)
4. Add env-variable `WALLARM_API_TOKEN` in the plan configuration
5. Rerun build. It will find vulnerabilities

## Useful links

- More about Wallarm FAST: https://wallarm.com/products/fast
- More about Azure DevOps: https://docs.microsoft.com/en-us/azure/devops/
- DVWA application's source code: https://github.com/wallarm/fast-example-dvwa
- Full FAST documentation: https://docs.fast.wallarm.com

Try Wallarm FAST now: https://fast.wallarm.com/signup
