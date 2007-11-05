
#include "FluxFunction.h"
#include "FluxParams.h"

FluxFunction::FluxFunction(void)
{
	params_ = FluxParams();
}

FluxFunction::FluxFunction(const FluxParams & params)
{
	params_ = FluxParams(params);
}

FluxFunction::~FluxFunction(void)
{
}

const FluxParams & FluxFunction::fluxParams(void)
{
	return params_;
}

void FluxFunction::fluxParams(const FluxParams & params)
{
	params_ = params;
}


