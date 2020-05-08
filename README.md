# Example of running FAST tests for DVWA in Bamboo  

This is an example of Wallarm FAST running security tests in the Bamboo 7.0.3 pipeline. The target application is DVWA.

## Quick start with the example

1. Fork this repository.
2. Create the FAST node and copy a token value by the [link](https://us1.my.wallarm.com/testing/nodes).
3. Add a copied token as the `WALLARM_API_TOKEN` global variable in Bamboo.
4. Create a plan with the name **FAST** in Bamboo.
5. Link forked repository to Bamboo enabling the **Allow Bamboo to scan this repository for YAML and Java Specs** option.
6. Add webhook URL specified in the **Linked repository** > **Bamboo Specs** section to the forked repository.
7. Proceed to the **Specs status** tab and click **Scan** to check Bamboo configuration files and run the plan.

## Useful links

- More about Wallarm FAST: https://wallarm.com/products/fast
- More about Azure DevOps: https://docs.microsoft.com/en-us/azure/devops/
- DVWA application's source code: https://github.com/wallarm/fast-example-dvwa
- Full FAST documentation: https://docs.fast.wallarm.com

Try Wallarm FAST now: https://fast.wallarm.com/signup
