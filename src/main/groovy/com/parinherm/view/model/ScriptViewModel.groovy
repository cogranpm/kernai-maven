package com.parinherm.view.model

import com.parinherm.domain.Script

class ScriptViewModel {
	List<Script> items = [new Script(name: "closures", body: "some groovy stuff here")]
}
