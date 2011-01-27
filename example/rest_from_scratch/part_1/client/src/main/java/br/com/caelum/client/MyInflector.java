package br.com.caelum.client;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jvnet.inflector.Rule;
import org.jvnet.inflector.RuleBasedPluralizer;
import org.jvnet.inflector.rule.IrregularMappingRule;

import static org.jvnet.inflector.rule.AbstractRegexReplacementRule.disjunction;
import static org.jvnet.inflector.rule.IrregularMappingRule.toMap;

public class MyInflector extends RuleBasedPluralizer 
{
	
	private static final Map<String, String> IRREGULAR_NOUNS = toMap(new String[][]
	{
		{ "item", "itens" },
	});

	private final List<Rule> rules = Arrays.asList(new Rule[] 
	{
		new IrregularMappingRule(IRREGULAR_NOUNS, "(?i)(.*)\\b" + disjunction(IRREGULAR_NOUNS.keySet()) + "$")
	});

	public MyInflector() 
	{
		setRules(rules);
		setLocale(Locale.ENGLISH);
	}

	@Override
	protected String postProcess(String trimmedWord, String pluralizedWord) 
	{
		if (trimmedWord.matches("^I$")) 
		{
			return pluralizedWord;
		} 
		return super.postProcess(trimmedWord, pluralizedWord);
	}

}